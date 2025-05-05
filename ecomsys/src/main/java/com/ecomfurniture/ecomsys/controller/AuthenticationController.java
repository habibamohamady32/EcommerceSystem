package com.ecomfurniture.ecomsys.controller;

import com.ecomfurniture.ecomsys.dtos.AuthResponseDTO;
import com.ecomfurniture.ecomsys.dtos.LoginDTO;
import com.ecomfurniture.ecomsys.security.AuthenticationService;
import com.ecomfurniture.ecomsys.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        String token = authenticationService.authenticateAndGenerateToken(loginDTO.getEmail(), loginDTO.getPassword());

        if (token != null) {
            return ResponseEntity.ok(new AuthResponseDTO(token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponseDTO("Invalid credentials"));
    }
}