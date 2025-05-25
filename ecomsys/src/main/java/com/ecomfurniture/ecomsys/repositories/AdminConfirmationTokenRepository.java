package com.ecomfurniture.ecomsys.repositories;

import com.ecomfurniture.ecomsys.entity.AdminConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminConfirmationTokenRepository extends JpaRepository<AdminConfirmationToken, Long> {
    Optional<AdminConfirmationToken> findByToken(String token);
}
