package com.ecomfurniture.ecomsys.services;

import com.ecomfurniture.ecomsys.entity.Admin;
import com.ecomfurniture.ecomsys.entity.User;
import com.ecomfurniture.ecomsys.repositories.AdminRepository;
import com.ecomfurniture.ecomsys.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VerificationServiceImpl implements VerificationService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public VerificationServiceImpl(UserRepository userRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public boolean verifyUserToken(String token) {
        Optional<User> optionalUser = userRepository.findByVerificationToken(token);
        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();

        // Check token expiration (assuming tokenCreatedAt is stored)
        if (user.getTokenCreatedAt().plusMinutes(15).isBefore(LocalDateTime.now())) {
            return false;
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null); // clear token after verification
        user.setTokenCreatedAt(null);
        userRepository.save(user);

        return true;
    }

    @Override
    public boolean verifyAdminToken(String token) {
        Optional<Admin> optionalAdmin = adminRepository.findByVerificationToken(token);
        if (optionalAdmin.isEmpty()) {
            return false;
        }

        Admin admin = optionalAdmin.get();

        if (admin.getTokenCreatedAt().plusMinutes(15).isBefore(LocalDateTime.now())) {
            return false;
        }

        admin.setEmailVerified(true);
        admin.setVerificationToken(null); // clear token after verification
        admin.setTokenCreatedAt(null);
        adminRepository.save(admin);

        return true;
    }
}
