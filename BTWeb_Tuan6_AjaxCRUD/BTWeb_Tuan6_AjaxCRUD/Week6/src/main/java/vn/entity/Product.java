package vn.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Products")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long productId;

    // Tên không được rỗng và có độ dài từ 3 đến 200 ký tự
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 3, max = 200, message = "Tên sản phẩm phải từ 3 đến 200 ký tự")
    @Column(name = "name", length = 200, columnDefinition = "nvarchar(200) not null")
    private String name;

    // Số lượng không được null và phải lớn hơn hoặc bằng 0
    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải là số không âm")
    @Column(name = "quantity")
    private int quantity;

    // Đơn giá không được null và phải lớn hơn hoặc bằng 0
    @NotNull(message = "Đơn giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Đơn giá phải là số không âm")
    @Column(name = "unit_price")
    private double unitPrice;

    // Giảm giá phải nằm trong khoảng từ 0 đến 100
    @Min(value = 0, message = "Giảm giá phải lớn hơn hoặc bằng 0")
    @Max(value = 100, message = "Giảm giá phải nhỏ hơn hoặc bằng 100")
    @Column(name = "discount")
    private double discount;

    @Column(name = "images", length = 255)
    private String images;

    @Column(name = "description", columnDefinition = "nvarchar(500)")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "YYYY-MM-DD hh:mi:ss")
    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "status")
    private short status;

    // Category không được null khi tạo/cập nhật sản phẩm
    @NotNull(message = "Sản phẩm phải thuộc về một danh mục")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
}