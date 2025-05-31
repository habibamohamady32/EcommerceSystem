package com.ecomfurniture.ecomsys.controller;

import com.ecomfurniture.ecomsys.dtos.ApiResponseDTO;
import com.ecomfurniture.ecomsys.dtos.RegisterAdminDTO;
import com.ecomfurniture.ecomsys.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterAdminDTO dto) {
        adminService.registerAdmin(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseDTO("Admin registered. Please verify your email."));

    }
}