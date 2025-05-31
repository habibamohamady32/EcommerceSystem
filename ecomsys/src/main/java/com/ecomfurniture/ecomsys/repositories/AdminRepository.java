package com.ecomfurniture.ecomsys.repositories;

import com.ecomfurniture.ecomsys.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByResetToken(String resetToken);
    Optional<Admin> findByVerificationToken(String token);
}
