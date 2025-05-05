package com.ecomfurniture.ecomsys.services;

import com.ecomfurniture.ecomsys.dtos.RegisterUserDTO;
import com.ecomfurniture.ecomsys.entity.Role;
import com.ecomfurniture.ecomsys.entity.RoleName;
import com.ecomfurniture.ecomsys.entity.User;
import com.ecomfurniture.ecomsys.entity.VerificationToken;
import com.ecomfurniture.ecomsys.repositories.RoleRepository;
import com.ecomfurniture.ecomsys.repositories.UserRepository;
import com.ecomfurniture.ecomsys.repositories.VerificationTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, VerificationTokenRepository tokenRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
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
        user.setPassword(dto.getPassword());
        user.setAddress(dto.getAddress());
        user.setPhonenumber(dto.getPhonenumber());

        Role userRole = roleRepository.findByRole(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("USER role not found"));
        user.setRole(userRole);

        user.setEmailVerified(false);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(verificationToken);

        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + token;
        String message = "Please verify your email using the following link: " + verificationLink;
        emailService.sendEmail(user.getEmail(), "Email Verification", message);
    }
}
