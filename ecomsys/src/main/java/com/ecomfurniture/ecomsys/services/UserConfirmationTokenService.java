package com.ecomfurniture.ecomsys.services;

import com.ecomfurniture.ecomsys.entity.User;
import com.ecomfurniture.ecomsys.entity.UserConfirmationToken;
import com.ecomfurniture.ecomsys.repositories.UserConfirmationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserConfirmationTokenService {

    private final UserConfirmationTokenRepository tokenRepository;

    public UserConfirmationTokenService(UserConfirmationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String createToken(User user) {
        String token = UUID.randomUUID().toString();
        UserConfirmationToken confirmationToken = new UserConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        tokenRepository.save(confirmationToken);
        return token;
    }

    public Optional<UserConfirmationToken> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        UserConfirmationToken confirmationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(confirmationToken);
    }
}

