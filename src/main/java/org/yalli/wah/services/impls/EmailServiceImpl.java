package org.yalli.wah.services.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.yalli.wah.services.EmailService;
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendConfirmationEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@domain.com");
        message.setTo(email);

        message.setSubject("Email Confirmation - Your OTP Code");

        // Email body with OTP token instead of a link
        String emailBody = String.format("Dear User,\n\nYour OTP for email verification is: %s\n\nPlease enter this code in the app to verify your email.\n\nThanks,\nYour Team", token);

        message.setText(emailBody);

        // Send the email
        mailSender.send(message);
    }
    @Override
    public void sendOtpEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP code is: " + otp);
        mailSender.send(message);
    }
}
