package vn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    /**
     * Hiển thị trang chủ cho người dùng có role USER
     */
    @GetMapping("/user/home")
    public String userHomePage() {
        return "user/home"; // Trỏ đến file templates/user/home.html
    }

    /**
     * Hiển thị trang dashboard chính cho người dùng có role ADMIN
     * Dựa theo cấu trúc của bạn, trang này sẽ là trang quản lý categories
     */
    @GetMapping("/admin/dashboard")
    public String adminDashboardPage() {
        // Giả sử trang chính của admin là trang quản lý category
        // Trỏ đến file templates/admin/categories/searchpaginated.html
    	 return "admin/ajax-crud";
    }
 
}