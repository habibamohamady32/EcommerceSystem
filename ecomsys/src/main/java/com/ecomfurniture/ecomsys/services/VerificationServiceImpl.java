package com.ecomfurniture.ecomsys.services;

import com.ecomfurniture.ecomsys.entity.Admin;
import com.ecomfurniture.ecomsys.entity.User;
import com.ecomfurniture.ecomsys.entity.AdminConfirmationToken;
import com.ecomfurniture.ecomsys.entity.UserConfirmationToken;
import com.ecomfurniture.ecomsys.repositories.AdminConfirmationTokenRepository;
import com.ecomfurniture.ecomsys.repositories.AdminRepository;
import com.ecomfurniture.ecomsys.repositories.UserConfirmationTokenRepository;
import com.ecomfurniture.ecomsys.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VerificationServiceImpl implements VerificationService {

    private final UserConfirmationTokenRepository userTokenRepository;
    private final AdminConfirmationTokenRepository adminTokenRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public VerificationServiceImpl(UserConfirmationTokenRepository userTokenRepository,
                                   AdminConfirmationTokenRepository adminTokenRepository,
                                   UserRepository userRepository,
                                   AdminRepository adminRepository) {
        this.userTokenRepository = userTokenRepository;
        this.adminTokenRepository = adminTokenRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public boolean verifyUserToken(String token) {
        Optional<UserConfirmationToken> optionalToken = userTokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            return false;
        }

        UserConfirmationToken confirmationToken = optionalToken.get();

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        User user = confirmationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        userTokenRepository.delete(confirmationToken);
        return true;
    }

    @Override
    public boolean verifyAdminToken(String token) {
        Optional<AdminConfirmationToken> optionalToken = adminTokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            return false;
        }

        AdminConfirmationToken confirmationToken = optionalToken.get();

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        Admin admin = confirmationToken.getAdmin();
        admin.setEmailVerified(true);
        adminRepository.save(admin);

        adminTokenRepository.delete(confirmationToken);
        return true;
    }
}
