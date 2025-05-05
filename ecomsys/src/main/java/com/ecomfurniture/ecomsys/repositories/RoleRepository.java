package com.ecomfurniture.ecomsys.repositories;

import com.ecomfurniture.ecomsys.entity.Role;
import com.ecomfurniture.ecomsys.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(RoleName role);
}
