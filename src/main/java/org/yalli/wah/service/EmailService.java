package org.yalli.wah.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendConfirmationEmail(String email,String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("delmer.dubuque81@ethereal.email");
        message.setTo(email);

        message.setSubject("Confirm Email - OTP");
        String emailBody = String.format("Your otp code for confirm email %s", otp);
        message.setText(emailBody);
        mailSender.send(message);
    }

    public void sendOtp(String email, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("tina.hilpert73@ethereal.email");
        message.setTo(email);

        message.setSubject("Reset Password - OTP");
        String emailBody = String.format("Your otp code for resettin password is %s", otp);
        message.setText(emailBody);
        mailSender.send(message);
    }
}
