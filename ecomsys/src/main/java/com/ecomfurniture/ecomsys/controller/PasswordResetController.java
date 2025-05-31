package com.ecomfurniture.ecomsys.controller;

import com.ecomfurniture.ecomsys.dtos.PasswordResetDTO;
import com.ecomfurniture.ecomsys.dtos.PasswordResetRequestDTO;
import com.ecomfurniture.ecomsys.services.AdminServiceImpl;
import com.ecomfurniture.ecomsys.services.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {

    private final AdminServiceImpl adminService;
    private final UserServiceImpl userService;

    public PasswordResetController(AdminServiceImpl adminService, UserServiceImpl userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestReset(@RequestBody PasswordResetRequestDTO dto) {
        boolean found = false;

        try {
            adminService.initiatePasswordReset(dto.getEmail());
            found = true;
        } catch (UsernameNotFoundException ignored) {
            // admin not found, try user
        }

        if (!found) {
            try {
                userService.initiatePasswordReset(dto.getEmail());
            } catch (UsernameNotFoundException ignored) {
                // user not found
            }
        }

        // Do not reveal whether the email was found for security reasons
        return ResponseEntity.ok("Reset email sent if account exists");
    }


    @PostMapping("/confirm")
    public ResponseEntity<?> resetPassword(
            @RequestParam("token") String token,
            @Valid @RequestBody PasswordResetDTO dto) {

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        boolean resetSuccessful = adminService.resetPassword(token, dto.getNewPassword());
        if (!resetSuccessful) {
            resetSuccessful = userService.resetPassword(token, dto.getNewPassword());
        }

        if (!resetSuccessful) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }

        return ResponseEntity.ok("Password reset successfully");
    }

}

