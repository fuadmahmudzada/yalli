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
        message.setTo(email);  // Recipient email

        message.setSubject("Email Confirmation - Please verify your email");


        String confirmationUrl = "http://localhost:2100/api/auth/confirm?email=" + email + "&token=" + token;

        // Email body message
        String emailBody = String.format("Dear User,\n\nPlease confirm your email by clicking the link below:\n%s\n\nThanks,\nYour Team", confirmationUrl);

        message.setText(emailBody);

        // Send the email
        mailSender.send(message);
    }
}
