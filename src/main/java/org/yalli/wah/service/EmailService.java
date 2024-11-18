package org.yalli.wah.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendMail(String email, String emailSubject, String emailBody){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("tina.hilpert73@ethereal.email");
        message.setTo(email);

        message.setSubject(emailSubject);
        message.setText(emailBody);
        mailSender.send(message);
    }
}
