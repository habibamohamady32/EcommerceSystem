package com.ecomfurniture.ecomsys.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponseDTO {
    private String token; //focused on login tokens

    public AuthResponseDTO(String token) {
        this.token = token;
    }
}