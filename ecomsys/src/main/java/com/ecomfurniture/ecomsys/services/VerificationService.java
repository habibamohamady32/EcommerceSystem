package com.ecomfurniture.ecomsys.services;

public interface VerificationService {
    boolean verifyUserToken(String token);
    boolean verifyAdminToken(String token);
}
