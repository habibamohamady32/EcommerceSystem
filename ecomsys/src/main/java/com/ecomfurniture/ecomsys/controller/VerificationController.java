package com.ecomfurniture.ecomsys.controller;

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
    private final VerificationServiceImpl verificationService;

    public VerificationController(VerificationServiceImpl verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        boolean isVerified = verificationService.verifyToken(token);
        if (isVerified) {
            return ResponseEntity.ok("Email successfully verified!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid or expired token.");
        }
    }
}
