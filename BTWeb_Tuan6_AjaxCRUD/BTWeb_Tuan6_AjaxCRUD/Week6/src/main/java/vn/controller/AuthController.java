package vn.controller;

import vn.model.User;
import vn.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping({"/", "/login"})
    public String showLoginForm(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            User user = (User) session.getAttribute("loggedInUser");
            if ("ADMIN".equals(user.getRole())) {
                // ---- Đảm bảo dòng này đúng ----
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/user/home";
            }
        }
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, 
                              @RequestParam String password, 
                              HttpSession session, 
                              Model model) {
        
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)){
            User user = userOptional.get();
            session.setAttribute("loggedInUser", user);

            if ("ADMIN".equals(user.getRole())) {
                // ---- Đảm bảo dòng này đúng ----
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/user/home";
            }
        } else {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}