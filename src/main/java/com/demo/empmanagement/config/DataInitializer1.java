package com.demo.empmanagement.config;

import com.demo.empmanagement.models.Department;
import com.demo.empmanagement.models.User;
import com.demo.empmanagement.repository.DepartmentRepository;
import com.demo.empmanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer1 {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, DepartmentRepository departmentRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .role("ADMIN")
                        .build();

                User user = User.builder()
                        .username("user")
                        .password(passwordEncoder.encode("user123"))
                        .role("USER")
                        .build();

                userRepository.save(admin);
                userRepository.save(user);
            }

            if (departmentRepository.count() == 0) {
                departmentRepository.save(Department.builder().name("HR").build());
                departmentRepository.save(Department.builder().name("IT").build());
                departmentRepository.save(Department.builder().name("Finance").build());
            }
        };
    }
}
