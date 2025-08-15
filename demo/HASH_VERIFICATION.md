# Chunk Upload Controller - Hash Verification Feature

## Overview

The ChunkUploadController now supports file integrity verification using SHA-256 hash validation at both chunk level and complete file level.

## Features

### 1. Chunk-Level Hash Verification

- Verify individual chunks during upload
- Optional parameter `chunkHash` in `/upload/chunk` endpoint
- Uses SHA-256 algorithm
- Returns detailed error response if hash mismatch

### 2. File-Level Hash Verification

- Verify complete file after all chunks are assembled
- Optional parameter `fileHash` in `/upload/complete` endpoint
- Uses SHA-256 algorithm
- File is deleted if hash verification fails

## API Endpoints

### POST /upload/chunk

**Parameters:**

- `uploadId` (required): Upload session ID
- `chunkIndex` (required): Index of the chunk (0-based)
- `totalChunks` (required): Total number of chunks
- `filename` (required): Name of the file
- `chunk` (required): MultipartFile containing chunk data
- `chunkHash` (optional): SHA-256 hash of the chunk data

**Response with successful hash verification:**

```json
{
  "received": 0
}
```

**Response with hash mismatch:**

```json
{
  "error": "Chunk hash verification failed",
  "expected": "provided_hash",
  "actual": "calculated_hash"
}
```

### POST /upload/complete

**Parameters:**

- `uploadId` (required): Upload session ID
- `filename` (required): Name of the final file
- `fileHash` (optional): SHA-256 hash of the complete file

**Response with successful hash verification:**

```json
{
  "file": "test.txt",
  "path": "/path/to/uploads/test.txt",
  "size": 11,
  "verified": true,
  "hash": "a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e"
}
```

**Response with hash mismatch:**

```json
{
  "error": "File hash verification failed",
  "expected": "provided_hash",
  "actual": "calculated_hash"
}
```

## Usage Examples

### 1. Calculate File Hash

```bash
# Calculate SHA-256 hash of a file
shasum -a 256 myfile.txt
```

### 2. Upload with Chunk Hash Verification

```bash
# Calculate chunk hash
CHUNK_HASH=$(shasum -a 256 chunk_file.txt | cut -d' ' -f1)

# Upload chunk with hash
curl -X POST "http://localhost:8080/upload/chunk" \
  -F "uploadId=your-upload-id" \
  -F "chunkIndex=0" \
  -F "totalChunks=1" \
  -F "filename=myfile.txt" \
  -F "chunk=@chunk_file.txt" \
  -F "chunkHash=${CHUNK_HASH}"
```

### 3. Complete Upload with File Hash Verification

```bash
# Calculate complete file hash
FILE_HASH=$(shasum -a 256 complete_file.txt | cut -d' ' -f1)

# Complete upload with hash verification
curl -X POST "http://localhost:8080/upload/complete" \
  -d "uploadId=your-upload-id" \
  -d "filename=myfile.txt" \
  -d "fileHash=${FILE_HASH}"
```

## Security Benefits

1. **Data Integrity**: Ensures uploaded data hasn't been corrupted during transmission
2. **Tamper Detection**: Detects if chunks or files have been modified
3. **Error Validation**: Provides clear feedback on hash mismatches
4. **Automatic Cleanup**: Deletes corrupted files automatically

## Implementation Details

### Hash Calculation Methods

- `calcSha256(byte[] data)`: Calculates SHA-256 hash of byte array
- `sha256OfFile(Path filePath)`: Calculates SHA-256 hash of file

### Error Handling

- Returns HTTP 400 for hash mismatches
- Returns HTTP 500 for internal errors
- Provides detailed error messages with expected vs actual hashes
- Automatic cleanup of corrupted files

## Best Practices

1. **Always verify critical files**: Use hash verification for important uploads
2. **Calculate hashes client-side**: Pre-calculate hashes on the client for efficiency
3. **Handle errors gracefully**: Check response status and error messages
4. **Use secure hash algorithms**: SHA-256 is recommended for security
5. **Validate hash format**: Ensure hashes are properly formatted hex strings
