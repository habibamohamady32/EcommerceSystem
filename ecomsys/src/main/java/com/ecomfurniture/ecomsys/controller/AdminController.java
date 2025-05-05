package com.ecomfurniture.ecomsys.controller;

import com.ecomfurniture.ecomsys.dtos.RegisterAdminDTO;
import com.ecomfurniture.ecomsys.services.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterAdminDTO dto) {
        adminService.registerAdmin(dto);
        return ResponseEntity.ok("Admin registered successfully. Check your email for verification.");
    }
}
