package com.gl.project.NotificationService.controller;

import com.gl.project.NotificationService.dto.NotificationRequest;
import com.gl.project.NotificationService.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController controller;

    @Test
    void testSendEmail() {

        NotificationRequest request = new NotificationRequest();
        request.setEmail("test@gmail.com");
        request.setSubject("Test");
        request.setMessage("Hello");

        when(notificationService.sendEmail(request))
                .thenReturn("Email sent successfully");

        String response = controller.sendEmail(request);

        assertEquals("Email sent successfully", response);
    }
}