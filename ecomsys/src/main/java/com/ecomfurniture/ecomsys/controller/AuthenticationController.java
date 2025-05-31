package com.ecomfurniture.ecomsys.controller;

import com.ecomfurniture.ecomsys.dtos.AuthResponseDTO;
import com.ecomfurniture.ecomsys.dtos.LoginDTO;
import com.ecomfurniture.ecomsys.services.AuthenticationService;
import jakarta.validation.Valid;
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
    public ResponseEntity<AuthResponseDTO> loginUser(@Valid @RequestBody LoginDTO dto) {
        String token = authenticationService.authenticateUser(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponseDTO> loginAdmin(@Valid @RequestBody LoginDTO dto) {
        String token = authenticationService.authenticateAdmin(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

}
