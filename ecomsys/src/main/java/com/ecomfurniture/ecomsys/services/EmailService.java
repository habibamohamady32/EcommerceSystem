package com.ecomfurniture.ecomsys.services;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
