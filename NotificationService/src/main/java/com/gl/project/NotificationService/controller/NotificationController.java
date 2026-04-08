package com.gl.project.NotificationService.controller;

import com.gl.project.NotificationService.dto.NotificationRequest;
import com.gl.project.NotificationService.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/email")
    public String sendEmail(@Valid @RequestBody NotificationRequest request) {
        return notificationService.sendEmail(request);
    }

    @PostMapping("/sms")
    public String sendSms(@Valid @RequestBody NotificationRequest request) {
        return notificationService.sendSms(request);
    }
}
