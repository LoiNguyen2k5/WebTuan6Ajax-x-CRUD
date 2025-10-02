package vn.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Fullname cannot be blank")
    @Column(name = "fullname", nullable = false) // Chỉ định rõ tên cột
    private String fullname;

    @NotBlank(message = "Username cannot be blank")
    @Column(name = "username", unique = true, nullable = false) // Chỉ định rõ tên cột
    private String username;
    
    // ---- ĐÂY LÀ PHẦN SỬA QUAN TRỌNG NHẤT ----
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6)
    // Tên trường trong Java là "password"
    // nhưng nó sẽ ánh xạ vào cột "passwd" trong database
    @Column(name = "passwd", nullable = false) 
    private String password;

    @Email(message = "Email should be valid")
    @Column(name = "email") // Chỉ định rõ tên cột
    private String email;

    @NotBlank(message = "Role cannot be blank")
    @Column(name = "role") // Chỉ định rõ tên cột
    private String role;
    
    // Constructor bây giờ sẽ sử dụng tên trường "password"
    public User(String fullname, String username, String password, String email, String role) {
        this.fullname = fullname;
        this.username = username;
        this.password = password; // Tên trường Java là password
        this.email = email;
        this.role = role;
    }
}