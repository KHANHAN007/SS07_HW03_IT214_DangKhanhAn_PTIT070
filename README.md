# SS07 HW03: Giao tiếp đồng bộ giữa Microservice bằng RestTemplate

## Mục tiêu

Bài này xây dựng luồng chuyển tiền cho hệ thống FinBank. `transaction-service` nhận yêu cầu chuyển tiền, sau đó gọi `account-service` bằng `RestTemplate` có `@LoadBalanced` để kiểm tra tài khoản, kiểm tra số dư, trừ tiền nguồn, cộng tiền đích và lưu bản ghi giao dịch.

## Module

```text
discovery-server    : Eureka Server, port 8761
api-gateway         : Gateway, port 8222
account-service     : Quản lý tài khoản, port 8081
transaction-service : Xử lý chuyển tiền, port 8082
```

## API Account Service

```text
GET /api/accounts/{accountNumber}
GET /api/accounts/{accountNumber}/balance
PUT /api/accounts/{accountNumber}/debit
PUT /api/accounts/{accountNumber}/credit
```

Ví dụ body cho debit/credit:

```json
{
  "amount": 2000000
}
```

## API Transaction Service

```text
POST /api/transactions/transfer
```

Gọi qua Gateway:

```text
POST http://localhost:8222/api/transactions/transfer
```

Body:

```json
{
  "fromAccountNumber": "1001",
  "toAccountNumber": "1002",
  "amount": 2000000,
  "description": "Chuyen tien thanh toan hoa don"
}
```

## Dữ liệu mẫu

Khi `account-service` khởi động, hệ thống tự tạo:

```text
1001 - 10,000,000 VND
1002 - 5,000,000 VND
```

## Thứ tự chạy

```bash
./gradlew :discovery-server:bootRun
./gradlew :account-service:bootRun
./gradlew :transaction-service:bootRun
./gradlew :api-gateway:bootRun
```

## Test case Postman

Collection nằm trong thư mục:

```text
postman/FinBank_SS07_HW03.postman_collection.json
```

Các case chính:

- Chuyển `2,000,000` từ `1001` sang `1002`: `SUCCESS`
- Chuyển `100,000,000` từ `1001` sang `1002`: `FAILED` vì không đủ số dư
- Chuyển từ `1001` sang `9999`: `FAILED` vì tài khoản đích không tồn tại

## Điểm chính của RestTemplate

Trong `transaction-service`, bean `RestTemplate` được cấu hình:

```java
@Bean
@LoadBalanced
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

Do đó code gọi:

```text
http://account-service/api/accounts/1001
```

thay vì hard-code `localhost:8081`.

