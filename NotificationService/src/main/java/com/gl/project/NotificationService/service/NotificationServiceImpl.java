package com.gl.project.NotificationService.service;

import com.gl.project.NotificationService.dto.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Override
    public String sendEmail(NotificationRequest request) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(env.getProperty("spring.mail.username")); // ADD THIS
            message.setTo(request.getEmail());
            message.setSubject(request.getSubject());
            message.setText(request.getMessage());

            mailSender.send(message);

            return "EMAIL SENT";

        } catch (Exception e) {
            e.printStackTrace();
            return "EMAIL FAILED";
        }
    }

    @Override
    public String sendSms(NotificationRequest request) {

        System.out.println("Fake SMS sent to: " + request.getPhoneNumber());
        System.out.println("Message: " + request.getMessage());

        return env.getProperty("NotificationService.SMS_SENT");
    }
}