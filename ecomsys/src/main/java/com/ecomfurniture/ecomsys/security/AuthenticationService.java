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
    private final JwtTokenUtil jwtTokenUtil;

    public AuthenticationService(UserRepository userRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String authenticateAndGenerateToken(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null && user.isEmailVerified() && passwordEncoder.matches(password, user.getPassword())) {
            return jwtTokenUtil.generateToken(email);
        }
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin != null && admin.isEmailVerified() && passwordEncoder.matches(password, admin.getPassword())) {
            return jwtTokenUtil.generateToken(email);
        }
        return null;
    }
}
