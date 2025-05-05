package com.ecomfurniture.ecomsys.services;

import com.ecomfurniture.ecomsys.dtos.RegisterAdminDTO;
import com.ecomfurniture.ecomsys.entity.Admin;
import com.ecomfurniture.ecomsys.entity.Role;
import com.ecomfurniture.ecomsys.entity.RoleName;
import com.ecomfurniture.ecomsys.entity.VerificationToken;
import com.ecomfurniture.ecomsys.repositories.AdminRepository;
import com.ecomfurniture.ecomsys.repositories.RoleRepository;
import com.ecomfurniture.ecomsys.repositories.VerificationTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepository adminRepository, VerificationTokenRepository tokenRepository, RoleRepository roleRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerAdmin(RegisterAdminDTO dto) {
        Admin admin = new Admin();
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setEmailVerified(false);

        Role adminRole = roleRepository.findByRole(RoleName.ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin role not found"));
        admin.setRole(adminRole);

        adminRepository.save(admin);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setAdmin(admin);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        tokenRepository.save(verificationToken);

        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + token;
        String message = "Please verify your email using the following link: " + verificationLink;
        emailService.sendEmail(admin.getEmail(), "Email Verification", message);
    }
}
