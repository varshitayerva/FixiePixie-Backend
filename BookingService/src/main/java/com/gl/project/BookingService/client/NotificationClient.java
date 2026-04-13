package com.gl.project.BookingService.client;

import com.gl.project.BookingService.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationClient {

    @PostMapping("/notify/email")
    String sendEmail(@RequestBody NotificationRequest request);

    @PostMapping("/notify/sms")
    String sendSms(@RequestBody NotificationRequest request);
}