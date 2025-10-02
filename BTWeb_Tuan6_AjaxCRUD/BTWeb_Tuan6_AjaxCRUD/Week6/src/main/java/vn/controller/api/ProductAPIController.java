package vn.controller.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import vn.entity.Category;
import vn.entity.Product;
import vn.model.ProductModel; // Import DTO mới
import vn.model.Response;
import vn.service.CategoryService;
import vn.service.IProductService;
import vn.service.IStorageService;

@RestController
@RequestMapping("/api/product")
public class ProductAPIController {

    @Autowired private IProductService productService;
    @Autowired private CategoryService categoryService;
    @Autowired private IStorageService storageService;

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productService.findAll();
        return new ResponseEntity<>(new Response(true, "Successfully retrieved all products", products), HttpStatus.OK);
    }

    // --- SỬA LẠI HOÀN TOÀN PHƯƠNG THỨC NÀY ---
    @PostMapping(value = "/addProduct", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> addProduct(@Valid @ModelAttribute ProductModel model, BindingResult bindingResult) {
        
        // 1. Kiểm tra validation
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(new Response(false, "Validation failed", errors), HttpStatus.BAD_REQUEST);
        }

        // 2. Kiểm tra sự tồn tại của Category
        Optional<Category> optCategory = categoryService.findById(model.getCategoryId());
        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Category not found", null), HttpStatus.BAD_REQUEST);
        }

        // 3. Tạo Entity Product và sao chép thuộc tính từ Model
        Product product = new Product();
        BeanUtils.copyProperties(model, product); // Sao chép các trường có cùng tên
        product.setCategory(optCategory.get());
        product.setCreateDate(new Date());

        // 4. Xử lý file ảnh
        if (model.getImageFile() != null && !model.getImageFile().isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String storedFilename = storageService.getStorageFilename(model.getImageFile(), uuid.toString());
            storageService.store(model.getImageFile(), storedFilename);
            product.setImages(storedFilename);
        }

        // 5. Lưu vào DB và trả về
        Product savedProduct = productService.save(product);
        return new ResponseEntity<>(new Response(true, "Product added successfully", savedProduct), HttpStatus.CREATED);
    }

    // --- SỬA LẠI HOÀN TOÀN PHƯƠNG THỨC NÀY ---
    @PutMapping(value = "/updateProduct", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> updateProduct(@Valid @ModelAttribute ProductModel model, BindingResult bindingResult) {
        // 1. Kiểm tra validation
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(new Response(false, "Validation failed", errors), HttpStatus.BAD_REQUEST);
        }
        
        // 2. Kiểm tra sự tồn tại của Product và Category
        Optional<Product> optProduct = productService.findById(model.getProductId());
        if (optProduct.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Product not found", null), HttpStatus.NOT_FOUND);
        }
        Optional<Category> optCategory = categoryService.findById(model.getCategoryId());
        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Category not found", null), HttpStatus.BAD_REQUEST);
        }

        // 3. Cập nhật Entity từ Model
        Product existingProduct = optProduct.get();
        // Sao chép các thuộc tính mới, trừ id và file ảnh
        BeanUtils.copyProperties(model, existingProduct, "productId", "imageFile"); 
        existingProduct.setCategory(optCategory.get());

        // 4. Xử lý file ảnh (nếu có file mới)
        if (model.getImageFile() != null && !model.getImageFile().isEmpty()) {
            String oldImage = existingProduct.getImages();
            UUID uuid = UUID.randomUUID();
            String newStoredFilename = storageService.getStorageFilename(model.getImageFile(), uuid.toString());
            storageService.store(model.getImageFile(), newStoredFilename);
            existingProduct.setImages(newStoredFilename);

            if (oldImage != null && !oldImage.isEmpty()) {
                try {
                    storageService.delete(oldImage);
                } catch (Exception e) {
                    System.err.println("Could not delete old image file: " + oldImage);
                }
            }
        }
        
        // 5. Lưu vào DB và trả về
        Product updatedProduct = productService.save(existingProduct);
        return new ResponseEntity<>(new Response(true, "Product updated successfully", updatedProduct), HttpStatus.OK);
    }
    
    // Giữ nguyên phương thức delete
    @DeleteMapping("/deleteProduct")
    public ResponseEntity<?> deleteProduct(@RequestParam("productId") Long productId) {
        // ... code của bạn vẫn đúng ...
        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Product not found with id: " + productId, null), HttpStatus.NOT_FOUND);
        }

        Product productToDelete = optProduct.get();
        try {
            String imageToDelete = productToDelete.getImages();
            if (imageToDelete != null && !imageToDelete.isEmpty()) {
                storageService.delete(imageToDelete);
            }
            productService.deleteById(productId);
        } catch (Exception e) {
             return new ResponseEntity<>(new Response(false, "Error deleting product: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new Response(true, "Product deleted successfully", null), HttpStatus.OK);
    }
}