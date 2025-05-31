package com.ecomfurniture.ecomsys.services;

import com.ecomfurniture.ecomsys.dtos.RegisterUserDTO;
import com.ecomfurniture.ecomsys.entity.Role;
import com.ecomfurniture.ecomsys.entity.RoleName;
import com.ecomfurniture.ecomsys.entity.User;
import com.ecomfurniture.ecomsys.repositories.RoleRepository;
import com.ecomfurniture.ecomsys.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(RegisterUserDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAddress(dto.getAddress());
        user.setPhonenumber(dto.getPhonenumber());
        user.setEmailVerified(false);

        Role userRole = roleRepository.findByRole(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("USER role not found"));
        user.setRole(userRole);

        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenCreatedAt(LocalDateTime.now());
        user.setTokenExpiresAt(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        String verificationLink = "http://localhost:8081/api/verify?token=" + token + "&userType=user";
        String message = """
        Hello %s,

        Thank you for registering with Furnituriko.

        Please verify your email using the link below (expires in 15 minutes):
        %s

        Regards,
        Furnituriko Team
        """.formatted(user.getUsername(), verificationLink);
        try {
            emailService.sendEmail(user.getEmail(), "Email Verification", message);
            System.out.println("✅ Verification email sent to " + user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Failed to send verification email to " + user.getEmail());
        }
    }
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiresAt(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        String link = "http://localhost:8081/api/password-reset/confirm?token=" + token;
        String body = "Hi " + user.getUsername() + ",\n\nTo reset your password, please click the link below:\n" + link + "\n\nThis link is valid for 15 minutes.";

        emailService.sendEmail(user.getEmail(), "Password Reset Request", body);
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        Optional<User> optional = userRepository.findByResetToken(token);
        if (optional.isEmpty()) return false;

        User user = optional.get();

        if (user.getResetTokenExpiresAt() == null || user.getResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpiresAt(null);
        userRepository.save(user);
        return true;
    }


}
