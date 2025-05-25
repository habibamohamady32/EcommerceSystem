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
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserConfirmationTokenService userConfirmationTokenService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           EmailService emailService, PasswordEncoder passwordEncoder,
                           UserConfirmationTokenService userConfirmationTokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.userConfirmationTokenService = userConfirmationTokenService;
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

        Role userRole = roleRepository.findByRole(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("USER role not found"));
        user.setRole(userRole);

        user.setEmailVerified(false);

        userRepository.save(user);

        String token = userConfirmationTokenService.createToken(user);

        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + token;
        String message = "Please verify your email using the following link: " + verificationLink;
        emailService.sendEmail(user.getEmail(), "Email Verification", message);
    }
}
