package com.gl.project.NotificationService.service;

import com.gl.project.NotificationService.dto.NotificationRequest;

public interface NotificationService {

    String sendEmail(NotificationRequest request);

    String sendSms(NotificationRequest request);
}
