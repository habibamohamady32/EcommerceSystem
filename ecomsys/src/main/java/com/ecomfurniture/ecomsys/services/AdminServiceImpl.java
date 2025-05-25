package com.ecomfurniture.ecomsys.services;

import com.ecomfurniture.ecomsys.dtos.RegisterAdminDTO;
import com.ecomfurniture.ecomsys.entity.Admin;
import com.ecomfurniture.ecomsys.entity.Role;
import com.ecomfurniture.ecomsys.entity.RoleName;
import com.ecomfurniture.ecomsys.repositories.AdminRepository;
import com.ecomfurniture.ecomsys.repositories.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepository adminRepository, RoleRepository roleRepository,
                            EmailService emailService, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerAdmin(RegisterAdminDTO dto) {
        Admin admin = new Admin();
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setEmailVerified(false);

        Role adminRole = roleRepository.findByRole(RoleName.ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin role not found"));
        admin.setRole(adminRole);

        String token = UUID.randomUUID().toString();
        admin.setVerificationToken(token);
        admin.setTokenCreatedAt(java.time.LocalDateTime.now());

        adminRepository.save(admin);

        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + token;
        String message = "Please verify your email using the following link: " + verificationLink;
        emailService.sendEmail(admin.getEmail(), "Email Verification", message);
    }
}
