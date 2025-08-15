# Resume Upload Functionality

Hệ thống chunk upload này hỗ trợ khả năng **resume** (tiếp tục) upload khi bị gián đoạn mạng.

## Cách hoạt động

### 1. **Chunk-based Upload**

- File được chia thành nhiều chunks nhỏ (mặc định 5MB)
- Mỗi chunk được upload riêng biệt và lưu thành file `.part`
- Chunks được đánh số thứ tự: `000000.part`, `000001.part`, etc.

### 2. **Resume Process**

#### Khi bị rớt mạng

1. Client có thể gọi `/upload/status?uploadId={uploadId}` để kiểm tra trạng thái
2. Hoặc gọi `/upload/resume?uploadId={uploadId}&totalChunks={totalChunks}` để xem chunk nào còn thiếu
3. Tiếp tục upload những chunk còn thiếu

#### Ví dụ Resume Flow

```bash
# 1. Khởi tạo upload
curl -X POST "http://localhost:8081/upload/init" \
  -H "Content-Type: application/json" \
  -d '{
    "filename": "video.mp4",
    "totalChunks": 10,
    "totalSize": 52428800,
    "fileHash": "abc123..."
  }'

# Response: {"uploadId": "uuid-123", "chunkSize": 5242880, ...}

# 2. Upload một số chunks (giả sử upload được chunks 0, 1, 2)
curl -X POST "http://localhost:8081/upload/chunk" \
  -F "uploadId=uuid-123" \
  -F "chunkIndex=0" \
  -F "totalChunks=10" \
  -F "filename=video.mp4" \
  -F "chunk=@chunk0.bin"

# ... upload chunks 1, 2 thành công
# ... rớt mạng tại chunk 3

# 3. Kiểm tra trạng thái để resume
curl "http://localhost:8081/upload/resume?uploadId=uuid-123&totalChunks=10"

# Response:
# {
#   "uploadId": "uuid-123",
#   "totalChunks": 10,
#   "completedChunks": [0, 1, 2],
#   "missingChunks": [3, 4, 5, 6, 7, 8, 9],
#   "progress": "30.00%",
#   "canResume": true,
#   "isComplete": false
# }

# 4. Tiếp tục upload những chunks còn thiếu
curl -X POST "http://localhost:8081/upload/chunk" \
  -F "uploadId=uuid-123" \
  -F "chunkIndex=3" \
  -F "totalChunks=10" \
  -F "filename=video.mp4" \
  -F "chunk=@chunk3.bin"

# ... upload tiếp các chunks 4, 5, 6, 7, 8, 9

# 5. Hoàn thành upload
curl -X POST "http://localhost:8081/upload/complete" \
  -H "Content-Type: application/json" \
  -d '{
    "uploadId": "uuid-123",
    "filename": "video.mp4",
    "fileHash": "abc123..."
  }'
```

## API Endpoints cho Resume

### 1. `/upload/status` - Kiểm tra trạng thái upload

```bash
GET /upload/status?uploadId={uploadId}
```

**Response:**

```json
{
  "uploadId": "uuid-123",
  "chunks": [0, 1, 2, 5, 7],
  "totalChunks": 5,
  "uploadedSize": 26214400,
  "canResume": true,
  "status": "in_progress"
}
```

### 2. `/upload/resume` - Xem chunks còn thiếu

```bash
GET /upload/resume?uploadId={uploadId}&totalChunks={totalChunks}
```

**Response:**

```json
{
  "uploadId": "uuid-123",
  "totalChunks": 10,
  "completedChunks": [0, 1, 2],
  "missingChunks": [3, 4, 5, 6, 7, 8, 9],
  "progress": "30.00%",
  "canResume": true,
  "isComplete": false
}
```

### 3. `/upload/chunk` với tham số `overwrite`

```bash
POST /upload/chunk
Form data:
- uploadId: uuid-123
- chunkIndex: 3
- totalChunks: 10
- filename: video.mp4
- chunk: (file)
- overwrite: true/false (mặc định false)
```

**Nếu chunk đã tồn tại và overwrite=false:**

```json
{
  "message": "Chunk already exists",
  "chunkIndex": 3,
  "skipped": true,
  "existing_size": 5242880
}
```

### 4. `/upload/cleanup` - Dọn dẹp upload bị bỏ dở

```bash
POST /upload/cleanup?uploadId={uploadId}
```

**Response:**

```json
{
  "message": "Upload cleaned up successfully",
  "uploadId": "uuid-123",
  "deletedFiles": 5
}
```

## Tính năng chính

### ✅ **Resume tự động**

- Chunks đã upload thành công được giữ lại
- Chỉ cần upload lại những chunks còn thiếu
- Không cần bắt đầu lại từ đầu

### ✅ **Kiểm tra trùng lặp**

- Chunk đã tồn tại sẽ được skip (trừ khi `overwrite=true`)
- Tiết kiệm băng thông và thời gian

### ✅ **Hash verification**

- Xác minh tính toàn vẹn của từng chunk
- Xác minh hash của file hoàn chỉnh

### ✅ **Progress tracking**

- Theo dõi tiến độ upload real-time
- Hiển thị % hoàn thành

### ✅ **Cleanup**

- Dọn dẹp uploads bị bỏ dở
- Quản lý storage hiệu quả

## Client Implementation Example

```javascript
class ResumableUploader {
  constructor(file, options = {}) {
    this.file = file;
    this.chunkSize = options.chunkSize || 5 * 1024 * 1024; // 5MB
    this.totalChunks = Math.ceil(file.size / this.chunkSize);
    this.uploadId = null;
  }

  async init() {
    const response = await fetch('/upload/init', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        filename: this.file.name,
        totalChunks: this.totalChunks,
        totalSize: this.file.size,
        fileHash: await this.calculateFileHash()
      })
    });
    const data = await response.json();
    this.uploadId = data.uploadId;
    return data;
  }

  async resume() {
    if (!this.uploadId) throw new Error('Must init first');
    
    const response = await fetch(`/upload/resume?uploadId=${this.uploadId}&totalChunks=${this.totalChunks}`);
    const data = await response.json();
    
    // Upload missing chunks
    for (const chunkIndex of data.missingChunks) {
      await this.uploadChunk(chunkIndex);
    }
    
    return this.complete();
  }

  async uploadChunk(chunkIndex) {
    const start = chunkIndex * this.chunkSize;
    const end = Math.min(start + this.chunkSize, this.file.size);
    const chunk = this.file.slice(start, end);
    
    const formData = new FormData();
    formData.append('uploadId', this.uploadId);
    formData.append('chunkIndex', chunkIndex);
    formData.append('totalChunks', this.totalChunks);
    formData.append('filename', this.file.name);
    formData.append('chunk', chunk);
    
    const response = await fetch('/upload/chunk', {
      method: 'POST',
      body: formData
    });
    
    return response.json();
  }

  async complete() {
    const response = await fetch('/upload/complete', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        uploadId: this.uploadId,
        filename: this.file.name,
        fileHash: await this.calculateFileHash()
      })
    });
    
    return response.json();
  }
}

// Usage
const uploader = new ResumableUploader(file);
await uploader.init();
await uploader.resume(); // Sẽ tự động resume từ chunk cuối cùng
```

## Lưu ý quan trọng

1. **Thời gian tồn tại**: Chunks được lưu trong thư mục tạm, cần có cơ chế cleanup tự động
2. **Concurrency**: Có thể upload nhiều chunks song song để tăng tốc độ
3. **Error handling**: Cần retry mechanism cho từng chunk khi gặp lỗi
4. **Security**: Validate uploadId để tránh path traversal attacks
5. **Storage**: Monitor disk space cho thư mục chunks

Với thiết kế này, việc rớt mạng không còn là vấn đề lớn vì có thể dễ dàng tiếp tục upload từ chunk cuối cùng thành công!
