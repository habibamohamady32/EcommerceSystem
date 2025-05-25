package com.ecomfurniture.ecomsys.repositories;

import com.ecomfurniture.ecomsys.entity.UserConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserConfirmationTokenRepository extends JpaRepository<UserConfirmationToken, Long> {
    Optional<UserConfirmationToken> findByToken(String token);
}
