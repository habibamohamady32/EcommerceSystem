package com.ecomfurniture.ecomsys.services;

import com.ecomfurniture.ecomsys.entity.Admin;
import com.ecomfurniture.ecomsys.entity.User;
import com.ecomfurniture.ecomsys.entity.VerificationToken;
import com.ecomfurniture.ecomsys.repositories.AdminRepository;
import com.ecomfurniture.ecomsys.repositories.UserRepository;
import com.ecomfurniture.ecomsys.repositories.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerificationServiceImpl implements VerificationService {
    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public VerificationServiceImpl(VerificationTokenRepository tokenRepository, UserRepository userRepository, AdminRepository adminRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    public boolean verifyToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElse(null);

        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        if (verificationToken.getUser() != null) {
            User user = verificationToken.getUser();
            user.setEmailVerified(true);
            userRepository.save(user);
        } else if (verificationToken.getAdmin() != null) {
            Admin admin = verificationToken.getAdmin();
            admin.setEmailVerified(true);
            adminRepository.save(admin);
        }

        tokenRepository.delete(verificationToken);
        return true;
    }
}
