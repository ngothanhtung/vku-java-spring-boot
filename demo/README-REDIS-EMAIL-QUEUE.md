# Redis Email Queue Documentation

## Tổng quan

Hệ thống Email Redis Queue cung cấp một giải pháp email queue sử dụng Redis như là message broker, giúp xử lý email một cách bất đồng bộ và có khả năng mở rộng cao.

## Cấu trúc

### 1. EmailRedisQueueConfig

Lớp cấu hình cho Redis email queue, bao gồm:

- **RedisTemplate**: Cấu hình template để tương tác với Redis
- **Serialization**: Sử dụng Jackson JSON serializer cho EmailMessage objects
- **ThreadPool**: Cấu hình executor cho xử lý email bất đồng bộ

### 2. EmailRedisQueueService

Service chính xử lý email queue với các chức năng:

- **addToQueue()**: Thêm email vào Redis queue
- **processEmailQueue()**: Xử lý email từ queue (chạy tự động mỗi 5 giây)
- **getQueueSize()**: Lấy số lượng email trong queue
- **clearQueue()**: Xóa tất cả email trong queue
- **peekQueue()**: Xem email trong queue mà không xóa

### 3. EmailRedisController

REST API endpoints để quản lý email queue:

#### POST `/api/email-redis/send`

Thêm email mới vào queue

```json
{
  "to": "user@example.com",
  "subject": "Test Email",
  "body": "This is a test email"
}
```

#### GET `/api/email-redis/queue/size`

Lấy kích thước hiện tại của queue

#### GET `/api/email-redis/queue/peek?start=0&end=9`

Xem 10 email đầu tiên trong queue

#### DELETE `/api/email-redis/queue/clear`

Xóa tất cả email trong queue

## Cài đặt và cấu hình

### 1. Dependencies (build.gradle)

```gradle
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
```

### 2. Application Properties

```properties
# Redis Configuration
spring.data.redis.host=${REDIS_HOST:localhost}
spring.data.redis.port=${REDIS_PORT:6379}
spring.data.redis.timeout=60000
spring.data.redis.database=0
```

### 3. Docker Compose

Redis service đã được thêm vào docker-compose.yml:

```yaml
redis:
  image: redis:7-alpine
  restart: always
  ports:
    - '6379:6379'
  volumes:
    - redis_data:/data
  healthcheck:
    test: ['CMD', 'redis-cli', 'ping']
    timeout: 10s
    retries: 5
    interval: 5s
    start_period: 10s
```

## Cách sử dụng

### 1. Gửi email qua API

```bash
curl -X POST http://localhost:8080/api/email-redis/send \
  -H "Content-Type: application/json" \
  -d '{
    "to": "user@example.com",
    "subject": "Test Subject",
    "body": "Test Body"
  }'
```

### 2. Kiểm tra queue size

```bash
curl http://localhost:8080/api/email-redis/queue/size
```

### 3. Xem emails trong queue

```bash
curl http://localhost:8080/api/email-redis/queue/peek?start=0&end=4
```

### 4. Xóa queue

```bash
curl -X DELETE http://localhost:8080/api/email-redis/queue/clear
```

## Ưu điểm của Redis Email Queue

1. **Persistence**: Email được lưu trữ trong Redis, không bị mất khi restart application
2. **Scalability**: Có thể có nhiều instances xử lý cùng một queue
3. **Performance**: Redis có hiệu suất cao cho queue operations
4. **Monitoring**: Dễ dàng monitor queue size và trạng thái
5. **Reliability**: Redis cung cấp persistence và replication options

## So sánh với Memory Queue

| Tiêu chí | Memory Queue | Redis Queue |
|----------|-------------|-------------|
| Persistence | ❌ Mất khi restart | ✅ Persistent |
| Scalability | ❌ Single instance | ✅ Multi-instance |
| Memory Usage | ❌ Sử dụng RAM của app | ✅ Separate Redis memory |
| Monitoring | ❌ Khó monitor | ✅ Redis monitoring tools |
| Setup Complexity | ✅ Đơn giản | ❌ Cần Redis server |

## Lưu ý

- Đảm bảo Redis server đang chạy trước khi start application
- Queue sử dụng FIFO (First In, First Out) pattern
- Email được xử lý mỗi 5 giây một lần
- Có thể tùy chỉnh thread pool settings trong EmailRedisQueueConfig
