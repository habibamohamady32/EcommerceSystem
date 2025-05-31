package com.ecomfurniture.ecomsys.controller;

import com.ecomfurniture.ecomsys.services.VerificationService;
import com.ecomfurniture.ecomsys.services.VerificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/verify")
public class VerificationController {
    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping
    public ResponseEntity<String> verifyEmail(
            @RequestParam("token") String token,
            @RequestParam("userType") String userType) {

        boolean isVerified;
        if ("admin".equalsIgnoreCase(userType)) {
            isVerified = verificationService.verifyAdminToken(token);
        } else if ("user".equalsIgnoreCase(userType)) {
            isVerified = verificationService.verifyUserToken(token);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid userType parameter, Use 'admin' or 'user'.");
        }

        if (isVerified) {
            return ResponseEntity.ok("Email successfully verified!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid or expired token.");
        }
    }
}
