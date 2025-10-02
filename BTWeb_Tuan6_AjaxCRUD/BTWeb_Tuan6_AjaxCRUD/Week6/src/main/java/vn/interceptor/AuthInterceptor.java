package vn.interceptor;
import vn.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        String uri = request.getRequestURI();

        // Nếu chưa đăng nhập, chuyển về trang login
        if (loggedInUser == null) {
            response.sendRedirect("/login");
            return false;
        }

        // Nếu đã đăng nhập, kiểm tra quyền
        if (uri.startsWith("/admin/") && !"ADMIN".equals(loggedInUser.getRole())) {
            // User thường cố truy cập trang admin
            response.sendRedirect("/user/home"); // Hoặc trang báo lỗi
            return false;
        }
        
        if (uri.startsWith("/user/") && !"USER".equals(loggedInUser.getRole()) && !"ADMIN".equals(loggedInUser.getRole())) {
            // Một role lạ nào đó (nếu có)
             response.sendRedirect("/login");
             return false;
        }

        return true; // Cho phép request đi tiếp
    }
}