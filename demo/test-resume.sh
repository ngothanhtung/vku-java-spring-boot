#!/bin/bash

# Test script for Resume Upload Functionality
# Chạy: chmod +x test-resume.sh && ./test-resume.sh

BASE_URL="http://localhost:8080"
TEST_FILE="test-video.mp4"
CHUNK_SIZE=1048576  # 1MB chunks for testing

echo "=== Testing Resume Upload Functionality ==="

# Create a test file
echo "Creating test file..."
dd if=/dev/urandom of=$TEST_FILE bs=1024 count=5120 2>/dev/null  # 5MB file
echo "Test file created: $(ls -lh $TEST_FILE)"

# Calculate file hash
FILE_HASH=$(shasum -a 256 $TEST_FILE | cut -d' ' -f1)
echo "File hash: $FILE_HASH"

# Split file into chunks
echo "Splitting file into chunks..."
split -b $CHUNK_SIZE -d $TEST_FILE chunk_
TOTAL_CHUNKS=$(ls chunk_* | wc -l | tr -d ' ')
echo "Total chunks: $TOTAL_CHUNKS"

# 1. Initialize upload
echo -e "\n1. Initializing upload..."
INIT_RESPONSE=$(curl -s -X POST "$BASE_URL/upload/init" \
  -H "Content-Type: application/json" \
  -d "{
    \"filename\": \"$TEST_FILE\",
    \"totalChunks\": $TOTAL_CHUNKS,
    \"totalSize\": $(stat -f%z $TEST_FILE),
    \"fileHash\": \"$FILE_HASH\"
  }")

echo "Init response: $INIT_RESPONSE"
UPLOAD_ID=$(echo $INIT_RESPONSE | jq -r '.uploadId')
echo "Upload ID: $UPLOAD_ID"

# 2. Upload first few chunks (simulate partial upload)
echo -e "\n2. Uploading first 3 chunks..."
for i in {0..2}; do
  CHUNK_FILE=$(printf "chunk_%02d" $i)
  if [ -f "$CHUNK_FILE" ]; then
    echo "Uploading chunk $i..."
    CHUNK_RESPONSE=$(curl -s -X POST "$BASE_URL/upload/chunk" \
      -F "uploadId=$UPLOAD_ID" \
      -F "chunkIndex=$i" \
      -F "totalChunks=$TOTAL_CHUNKS" \
      -F "filename=$TEST_FILE" \
      -F "chunk=@$CHUNK_FILE")
    echo "Chunk $i response: $CHUNK_RESPONSE"
  fi
done

# 3. Check status (simulate checking after network interruption)
echo -e "\n3. Checking upload status..."
STATUS_RESPONSE=$(curl -s "$BASE_URL/upload/status?uploadId=$UPLOAD_ID")
echo "Status: $STATUS_RESPONSE"

# 4. Check resume info
echo -e "\n4. Getting resume information..."
RESUME_RESPONSE=$(curl -s "$BASE_URL/upload/resume?uploadId=$UPLOAD_ID&totalChunks=$TOTAL_CHUNKS")
echo "Resume info: $RESUME_RESPONSE"

# 5. Try uploading an existing chunk (should be skipped)
echo -e "\n5. Trying to re-upload chunk 1 (should be skipped)..."
DUPLICATE_RESPONSE=$(curl -s -X POST "$BASE_URL/upload/chunk" \
  -F "uploadId=$UPLOAD_ID" \
  -F "chunkIndex=1" \
  -F "totalChunks=$TOTAL_CHUNKS" \
  -F "filename=$TEST_FILE" \
  -F "chunk=@chunk_01")
echo "Duplicate chunk response: $DUPLICATE_RESPONSE"

# 6. Resume upload (upload remaining chunks)
echo -e "\n6. Resuming upload (uploading remaining chunks)..."
MISSING_CHUNKS=$(echo $RESUME_RESPONSE | jq -r '.missingChunks[]')
for chunk_index in $MISSING_CHUNKS; do
  CHUNK_FILE=$(printf "chunk_%02d" $chunk_index)
  if [ -f "$CHUNK_FILE" ]; then
    echo "Uploading missing chunk $chunk_index..."
    CHUNK_RESPONSE=$(curl -s -X POST "$BASE_URL/upload/chunk" \
      -F "uploadId=$UPLOAD_ID" \
      -F "chunkIndex=$chunk_index" \
      -F "totalChunks=$TOTAL_CHUNKS" \
      -F "filename=$TEST_FILE" \
      -F "chunk=@$CHUNK_FILE")
    echo "Chunk $chunk_index response: $CHUNK_RESPONSE"
  fi
done

# 7. Complete upload
echo -e "\n7. Completing upload..."
COMPLETE_RESPONSE=$(curl -s -X POST "$BASE_URL/upload/complete" \
  -H "Content-Type: application/json" \
  -d "{
    \"uploadId\": \"$UPLOAD_ID\",
    \"filename\": \"$TEST_FILE\",
    \"fileHash\": \"$FILE_HASH\"
  }")
echo "Complete response: $COMPLETE_RESPONSE"

# 8. Verify uploaded file
echo -e "\n8. Verifying uploaded file..."
UPLOADED_FILE_PATH=$(echo $COMPLETE_RESPONSE | jq -r '.path')
if [ -f "$UPLOADED_FILE_PATH" ]; then
  UPLOADED_HASH=$(shasum -a 256 "$UPLOADED_FILE_PATH" | cut -d' ' -f1)
  echo "Original hash:  $FILE_HASH"
  echo "Uploaded hash:  $UPLOADED_HASH"
  
  if [ "$FILE_HASH" = "$UPLOADED_HASH" ]; then
    echo "✅ SUCCESS: File uploaded and verified successfully!"
  else
    echo "❌ ERROR: Hash mismatch!"
  fi
else
  echo "❌ ERROR: Uploaded file not found at $UPLOADED_FILE_PATH"
fi

# Cleanup
echo -e "\n9. Cleaning up test files..."
rm -f chunk_* $TEST_FILE
echo "Test completed!"
