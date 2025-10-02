package vn.config;

import vn.model.User;
import vn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Sửa lại lời gọi constructor cho đúng 4 tham số
        	User admin = new User("Admin Fullname", "admin", "password123", "admin@example.com", "ADMIN");
        	User user = new User("User Fullname", "user", "password123", "user@example.com", "USER");

            userRepository.save(admin);
            userRepository.save(user);

            System.out.println("Created sample users: admin/password123 and user/password123");
        }
    }
}