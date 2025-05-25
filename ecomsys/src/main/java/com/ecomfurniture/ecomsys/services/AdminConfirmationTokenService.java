package com.ecomfurniture.ecomsys.services;

import com.ecomfurniture.ecomsys.entity.Admin;
import com.ecomfurniture.ecomsys.entity.AdminConfirmationToken;
import com.ecomfurniture.ecomsys.repositories.AdminConfirmationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminConfirmationTokenService {

    private final AdminConfirmationTokenRepository tokenRepository;

    public AdminConfirmationTokenService(AdminConfirmationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String createToken(Admin admin) {
        String token = UUID.randomUUID().toString();
        AdminConfirmationToken confirmationToken = new AdminConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                admin
        );
        tokenRepository.save(confirmationToken);
        return token;
    }

    public Optional<AdminConfirmationToken> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        AdminConfirmationToken confirmationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(confirmationToken);
    }
}

