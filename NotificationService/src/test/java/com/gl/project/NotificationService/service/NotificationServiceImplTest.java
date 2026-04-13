package com.gl.project.NotificationService.service;

import com.gl.project.NotificationService.dto.NotificationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private Environment env;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void testSendEmail() {

        NotificationRequest request = new NotificationRequest();
        request.setEmail("test@gmail.com");
        request.setSubject("Test Subject");
        request.setMessage("Test Message");

        when(env.getProperty("NotificationService.EMAIL_SENT"))
                .thenReturn("Email sent successfully");

        String response = notificationService.sendEmail(request);

        verify(mailSender, times(1))
                .send(any(SimpleMailMessage.class));

        assertEquals("Email sent successfully", response);
    }

    @Test
    void testSendSms() {

        NotificationRequest request = new NotificationRequest();
        request.setPhoneNumber("9876543210");
        request.setMessage("Test SMS");

        when(env.getProperty("NotificationService.SMS_SENT"))
                .thenReturn("SMS sent successfully (fake)");

        String response = notificationService.sendSms(request);

        assertEquals("SMS sent successfully (fake)", response);
    }
}