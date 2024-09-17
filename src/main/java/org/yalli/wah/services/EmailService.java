package org.yalli.wah.services;

public interface EmailService {
    void sendConfirmationEmail(String email, String token);
    void sendOtpEmail(String email, String otp);
}
