package com.ecomfurniture.ecomsys.controller;

import com.ecomfurniture.ecomsys.dtos.ApiResponseDTO;
import com.ecomfurniture.ecomsys.dtos.AuthResponseDTO;
import com.ecomfurniture.ecomsys.dtos.RegisterUserDTO;
import com.ecomfurniture.ecomsys.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDTO dto) {
        userService.registerUser(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseDTO("User registered successfully. Check your email for verification."));

    }
}
