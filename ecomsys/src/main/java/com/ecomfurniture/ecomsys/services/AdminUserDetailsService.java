package com.ecomfurniture.ecomsys.services;

import com.ecomfurniture.ecomsys.entity.Admin;
import com.ecomfurniture.ecomsys.repositories.AdminRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service("adminDetailsService")
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Admin not found with email: " + email));

        String roleName = "ROLE_" + admin.getRole().getRole().name();

        return new org.springframework.security.core.userdetails.User(
                admin.getEmail(),
                admin.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(roleName))
        );
    }
}

