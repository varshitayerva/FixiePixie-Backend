package com.gl.app.PaymentMicroservice.Client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "BOOKING-SERVICE")
public interface BookingClient {

    @PatchMapping("/bookings/{id}/status")
    void updateBookingStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status
    );

}
