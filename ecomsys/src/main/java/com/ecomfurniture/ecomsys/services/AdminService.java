package com.ecomfurniture.ecomsys.services;

import com.ecomfurniture.ecomsys.dtos.RegisterAdminDTO;

public interface AdminService {
    void registerAdmin(RegisterAdminDTO dto);
    boolean resetPassword(String token, String newPassword);
}
