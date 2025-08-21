# 📝 Đề bài kiểm tra Spring Boot REST API

## Mô tả yêu cầu

Bạn được yêu cầu xây dựng một ứng dụng **Spring Boot REST API** để quản lý thông tin nhân sự (`Employee`). 

- Sử dụng Tool `Spring initializr` với `Gradle`
- Quay lại toàn bộ quá trình code (Sử dụng Bandicam, OBS STUDIO, Camtasia) và up lên youtube cá nhân. Lấy link để share.

## 1. Thực thể `Employee`

Tạo một Entity `Employee` với các trường sau:

| Thuộc tính       | Kiểu dữ liệu    | Mô tả                                                 |
| ---------------- | --------------- | ----------------------------------------------------- |
| `id`             | `Long`          | Khóa chính, tự động tăng                              |
| `fullName`       | `String`        | Họ và tên nhân sự, tối thiểu 4 và tối đa 160 ký tự.                                 |
| `email`          | `String`        | Email, unique  , Đúng định dạng email                                       |
| `dateOfBirth`    | `LocalDate`     | Ngày sinh                                             |
| `gender`         | `Enum`          | Nam, Nữ, Khác                                         |
| `phoneNumber`    | `String`        | Số điện thoại, Đủ 10 kí tự.                                         |
| `active`         | `Boolean`       | Trạng thái hoạt động                                  |
| `hashedPassword` | `String`        | Mật khẩu đã mã hóa (lưu vào DB)                       |
| `createdAt`      | `LocalDateTime` | Ngày tạo                                              |
| `updatedAt`      | `LocalDateTime` | Ngày cập nhật                                         |

👉 Lưu ý:

* Khi tạo mới `Employee`, service sẽ tự động **hash** và lưu vào `hashedPassword`.

---

## 2. Các API cần xây dựng

1. `POST /api/employees` – Tạo mới employee

   * Input: JSON Employee (có `password`)
   * Output: Employee DTO (không chứa hashedPassword)

2. `GET /api/employees` – Lấy danh sách tất cả nhân sự

3. `GET /api/employees/{id}` – Lấy thông tin chi tiết một nhân sự theo ID

4. `PUT /api/employees/{id}` – Cập nhật thông tin nhân sự (ngoại trừ email)

5. `DELETE /api/employees/{id}` – Xóa nhân sự

---

## 3. Yêu cầu bổ sung

1. Sử dụng `Lombok` để giảm boilerplate code.
2. Không trả về `hashedPassword` trong response.
3. Code phải rõ ràng, tách biệt từng layer (Controller – Service – Repository).
4. Response về một cấu trúc chuẩn khi lỗi và thành công.
5. Đẩy lên code hoàn thiện github cá nhân

## 4. Hướng dẫn nộp bài


