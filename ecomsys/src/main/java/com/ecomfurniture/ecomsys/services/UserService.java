package com.ecomfurniture.ecomsys.services;

import com.ecomfurniture.ecomsys.dtos.RegisterUserDTO;

public interface UserService {
    void registerUser(RegisterUserDTO dto);
    boolean resetPassword(String token, String newPassword);
}
