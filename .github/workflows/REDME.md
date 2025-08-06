
# GitHub Action for Java Spring Boot Project

## 📋 Chức năng của GitHub Action

### Trigger điều kiện

- Khi push code lên nhánh `main`
- Khi tạo tag bắt đầu với `deployment` (vd: `deployment-v1.0`, `deployment-prod`)

### Các bước thực hiện

#### 1. Setup môi trường

- Checkout code
- Setup JDK 21 (Amazon Corretto)
- Cache Gradle dependencies để tăng tốc build

#### 2. Build và test

- Chạy tests với `./gradlew test`
- Build JAR file với `./gradlew clean bootJar`

#### 3. Docker operations

- Setup Docker Buildx
- Login vào Docker Hub
- Build và push Docker image với multi-platform support (linux/amd64, linux/arm64)

#### 4. Tagging strategy

- Tag với tên branch (main)
- Tag với tên tag nếu có
- Tag `latest` cho branch chính

## 🔧 Cấu hình cần thiết

Bạn cần thêm 2 secrets vào repository GitHub:

- **DOCKERHUB_USERNAME**: Username Docker Hub của bạn
- **DOCKERHUB_TOKEN**: Access token Docker Hub (không phải password)

### Cách tạo Docker Hub Access Token

1. Đăng nhập Docker Hub → Account Settings → Security
2. Tạo New Access Token với quyền Read, Write, Delete
3. Copy token và lưu vào GitHub Secrets

### Cách thêm GitHub Secrets

1. Vào repository GitHub → Settings → Secrets and variables → Actions
2. Thêm 2 secrets trên

## 🚀 Cách sử dụng

### Trigger bằng push lên main

```bash
git push origin main
```

### Trigger bằng tag deployment

```bash
git tag deployment-v1.0
git push origin deployment-v1.0
```

## 📦 Kết quả

Image sẽ được push lên Docker Hub với tên `thanhtungo/vku-java-spring-boot-demo` theo các tag khác nhau.

Workflow này được tối ưu với cache Gradle và Docker layer caching để build nhanh hơn trong các lần chạy tiếp theo!

