package com.ecomfurniture.ecomsys.security;

import com.ecomfurniture.ecomsys.entity.Admin;
import com.ecomfurniture.ecomsys.entity.User;
import com.ecomfurniture.ecomsys.repositories.AdminRepository;
import com.ecomfurniture.ecomsys.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || !user.isEmailVerified()) {
            return false;
        }

        return passwordEncoder.matches(password, user.getPassword());
    }

    public boolean authenticateAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin == null || !admin.isEmailVerified()) {
            return false;
        }

        return passwordEncoder.matches(password, admin.getPassword());
    }

    public String authenticateAndGenerateToken(String email, String password) {
        boolean isAuthenticated = authenticateUser(email, password);
        if (isAuthenticated) {
            return JwtTokenUtil.generateToken(email);
        }
        return null;
    }

}
