package org.yalli.wah.services.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.yalli.wah.services.EmailService;
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;  // Inject JavaMailSender to send emails

    @Override
    public void sendConfirmationEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@domain.com");  // Sender email (should be valid)
        message.setTo(email);  // Recipient email

        message.setSubject("Email Confirmation - Please verify your email");

        // Construct the email confirmation URL
        String confirmationUrl = "http://localhost:5050/api/auth/confirm?email=" + email + "&token=" + token;

        // Email body message
        String emailBody = String.format("Dear User,\n\nPlease confirm your email by clicking the link below:\n%s\n\nThanks,\nYour Team", confirmationUrl);

        message.setText(emailBody);

        // Send the email
        mailSender.send(message);
    }
}
