package com.ecomfurniture.ecomsys.controller;

import com.ecomfurniture.ecomsys.dtos.AuthResponseDTO;
import com.ecomfurniture.ecomsys.dtos.LoginDTO;
import com.ecomfurniture.ecomsys.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/user/login")
    public ResponseEntity<AuthResponseDTO> userLogin(@RequestBody LoginDTO loginDTO) {
        String token = authenticationService.authenticateAndGenerateToken(loginDTO.getEmail(), loginDTO.getPassword());
        if (token != null) {
            return ResponseEntity.ok(new AuthResponseDTO(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponseDTO("Invalid user credentials"));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponseDTO> adminLogin(@RequestBody LoginDTO loginDTO) {
        String token = authenticationService.authenticateAndGenerateToken(loginDTO.getEmail(), loginDTO.getPassword());
        if (token != null) {
            return ResponseEntity.ok(new AuthResponseDTO(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponseDTO("Invalid admin credentials"));
    }
}
