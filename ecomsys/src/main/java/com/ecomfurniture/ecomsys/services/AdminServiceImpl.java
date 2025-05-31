package com.ecomfurniture.ecomsys.services;

import com.ecomfurniture.ecomsys.dtos.RegisterAdminDTO;
import com.ecomfurniture.ecomsys.entity.Admin;
import com.ecomfurniture.ecomsys.entity.Role;
import com.ecomfurniture.ecomsys.entity.RoleName;
import com.ecomfurniture.ecomsys.repositories.AdminRepository;
import com.ecomfurniture.ecomsys.repositories.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
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
        if (adminRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

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
        admin.setTokenCreatedAt(LocalDateTime.now());
        admin.setTokenExpiresAt(LocalDateTime.now().plusMinutes(15));

        adminRepository.save(admin);

        String verificationLink = "http://localhost:8081/api/verify?token=" + token + "&userType=admin";
        String message = """
        Hello %s,

        Thank you for registering with Furnituriko.

        Please verify your email using the link below (expires in 15 minutes):
        %s

        Regards,
        Furnituriko Team
        """.formatted(admin.getUsername(), verificationLink);
        try {
            emailService.sendEmail(admin.getEmail(), "Email Verification", message);
            System.out.println("✅ Verification email sent to " + admin.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Failed to send verification email to " + admin.getEmail());
        }
    }

    public void initiatePasswordReset(String email) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        String token = UUID.randomUUID().toString();
        admin.setResetToken(token);
        admin.setResetTokenExpiresAt(LocalDateTime.now().plusMinutes(15));
        adminRepository.save(admin);

        String link = "http://localhost:8081/api/password-reset/confirm?token=" + token;
        String body = "Hi " + admin.getUsername() + ",\n\nTo reset your password, please click the link below:\n" + link + "\n\nThis link is valid for 15 minutes.";

        emailService.sendEmail(admin.getEmail(), "Password Reset Request", body);
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        Optional<Admin> optional = adminRepository.findByResetToken(token);
        if (optional.isEmpty()) return false;

        Admin admin = optional.get();

        if (admin.getResetTokenExpiresAt() == null || admin.getResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        admin.setPassword(passwordEncoder.encode(newPassword));
        admin.setResetToken(null);
        admin.setTokenExpiresAt(null);
        adminRepository.save(admin);
        return true;
    }


}
