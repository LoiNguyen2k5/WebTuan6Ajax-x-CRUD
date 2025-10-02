package vn.model;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {

    private Long productId;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 3, max = 200, message = "Tên sản phẩm phải từ 3 đến 200 ký tự")
    private String name;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải là số không âm")
    private int quantity;

    @NotNull(message = "Đơn giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Đơn giá phải là số không âm")
    private double unitPrice;

    @Min(value = 0, message = "Giảm giá phải lớn hơn hoặc bằng 0")
    @Max(value = 100, message = "Giảm giá phải nhỏ hơn hoặc bằng 100")
    private double discount = 0;

    private String description;

    private short status = 1;

    @NotNull(message = "Sản phẩm phải thuộc về một danh mục")
    private Long categoryId;

    private MultipartFile imageFile;
}