# Quản lý Sản phẩm & Phân quyền với Spring Boot (Bài tập 10)

**Mã số sinh viên: 231110256**
**Tên: Nguyễn Bảo Lợi**

Dự án này là một ứng dụng web hoàn chỉnh được xây dựng bằng Spring Boot, kế thừa và phát triển từ project "Quản lý Sản phẩm & Danh mục". Phiên bản này bổ sung các tính năng bảo mật quan trọng bao gồm **đăng nhập, phân quyền theo vai trò (role-based authorization)** và **kiểm tra dữ liệu đầu vào (validation)**.

---

## ✨ Các tính năng chính

### Chức năng gốc
*   **CRUD đầy đủ** cho Danh mục (Category) và Sản phẩm (Product).
*   Giao diện quản lý sử dụng **AJAX**, mang lại trải nghiệm mượt mà không cần tải lại trang.
*   Chức năng **upload hình ảnh** cho cả Category và Product.
*   Tự động tạo tài liệu API với **Swagger UI**.
*   Bao gồm cả các trang quản lý theo kiểu MVC truyền thống (phân trang và tìm kiếm).

### Chức năng mới (Bài tập 10)
*   **Hệ thống Đăng nhập:** Người dùng cần đăng nhập để truy cập vào các chức năng của hệ thống.
*   **Phân quyền bằng Interceptor:**
    *   Tài khoản có vai trò `ADMIN` có thể truy cập vào trang quản lý (`/admin/**`).
    *   Tài khoản có vai trò `USER` chỉ có thể truy cập vào trang người dùng (`/user/**`).
    *   Các truy cập không đúng quyền hoặc chưa đăng nhập sẽ bị chặn và chuyển hướng về trang đăng nhập.
*   **Kiểm tra dữ liệu bằng Validator:**
    *   Tất cả dữ liệu đầu vào khi tạo hoặc cập nhật (ví dụ: tên sản phẩm, giá cả) đều được kiểm tra tính hợp lệ ở phía server.
    *   Nếu dữ liệu không hợp lệ, hệ thống sẽ trả về lỗi chi tiết thay vì lưu vào cơ sở dữ liệu.

---

## 🚀 Công nghệ sử dụng

*   **Backend:** Java 17, Spring Boot 3, Spring Web MVC, Spring Data JPA, Spring Validation.
*   **Frontend:** Thymeleaf, HTML5, CSS3, JavaScript (AJAX/Fetch), Bootstrap 5.
*   **Database:** Microsoft SQL Server.
*   **Build Tool:** Maven.

---

## 🛠️ Hướng dẫn Cài đặt và Chạy dự án

### 1. Yêu cầu môi trường
-   JDK 17 trở lên.
-   Maven 3.6 trở lên.
-   Microsoft SQL Server đã được cài đặt và đang chạy.

### 2. Cài đặt Cơ sở dữ liệu
Mở SQL Server Management Studio (SSMS).
1.  Tạo một database mới với tên là `Web_week5`.
2.  Mở một cửa sổ Query mới cho database `Web_week5` và chạy đoạn script sau để tạo các bảng `categories`, `products`, và `users`.

```sql
-- Tạo bảng Categories
CREATE TABLE dbo.categories (
    id BIGINT IDENTITY(1,1) NOT NULL,
    name NVARCHAR(200) NOT NULL,
    icon VARCHAR(255) NULL,
    PRIMARY KEY (id)
);
GO

-- Tạo bảng Products
CREATE TABLE dbo.products (
    id BIGINT IDENTITY(1,1) NOT NULL,
    name NVARCHAR(200) NOT NULL,
    quantity INT NOT NULL,
    unit_price FLOAT NOT NULL,
    discount FLOAT NOT NULL,
    images VARCHAR(255) NULL,
    description NVARCHAR(500) NULL,
    create_date DATETIME NOT NULL,
    status SMALLINT NOT NULL,
    category_id BIGINT NULL,
    PRIMARY KEY (id)
);
GO

-- Tạo bảng Users
CREATE TABLE dbo.users (
	id INT IDENTITY(1,1) NOT NULL,
	fullname NVARCHAR(255) NOT NULL,
	username VARCHAR(50) NOT NULL,
	email VARCHAR(255) NULL,
	passwd VARCHAR(255) NOT NULL, -- Chú ý: tên cột là 'passwd'
	role VARCHAR(50) NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (username)
);
GO

-- Thêm ràng buộc khóa ngoại từ Products đến Categories
ALTER TABLE dbo.products
ADD CONSTRAINT FK_products_categories
FOREIGN KEY (category_id) REFERENCES dbo.categories(id);
GO
```

### 3. Cấu hình ứng dụng
-   Mở file `src/main/resources/application.properties`.
-   Tìm đến các dòng cấu hình database và chỉnh sửa cho phù hợp với môi trường của bạn, đặc biệt là mật khẩu.
    ```properties
    spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=test_db;encrypt=true;trustServerCertificate=true;
    spring.datasource.username=sa
    spring.datasource.password=your_strong_password_here # <-- THAY MẬT KHẨU CỦA BẠN VÀO ĐÂY
    ```
-   Nếu server của bạn chạy trên cổng khác 8088, hãy sửa lại dòng `server.port=8088`.

### 4. Chạy ứng dụng
-   Build dự án bằng Maven:
    ```bash
    mvn clean install
    ```
-   Chạy file `Week6Application.java` từ IDE của bạn hoặc dùng lệnh:
    ```bash
    mvn spring-boot:run
    ```

---

## 🌐 Hướng dẫn sử dụng

Sau khi ứng dụng khởi động, luồng truy cập sẽ như sau:

#### 1. Trang Đăng nhập (Điểm bắt đầu)
Tất cả truy cập đều bắt đầu tại đây.
```
http://localhost:8088/login
```
Ứng dụng đã tạo sẵn 2 tài khoản mẫu:
-   **Admin:** `admin` / `password123`
-   **User:** `user` / `password123`

#### 2. Trang Admin
Sau khi đăng nhập với tài khoản `admin`, bạn sẽ được chuyển hướng đến trang quản lý chính.
```
http://localhost:8088/admin/dashboard
```
Tại đây, bạn có thể thực hiện các thao tác CRUD với Product và Category.

#### 3. Trang User
Sau khi đăng nhập với tài khoản `user`, bạn sẽ được chuyển hướng đến trang chủ của người dùng.
```
http://localhost:8088/user/home
```

#### 4. Giao diện Swagger
Công cụ để kiểm tra các REST API của bạn một cách trực quan.
```
http://localhost:8088/swagger-ui.html
```
**Lưu ý:** Các API (`/api/**`) cũng được bảo vệ. Bạn cần đăng nhập với vai trò `ADMIN` trước khi có thể sử dụng các API này.