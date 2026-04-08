package com.gl.project.BookingService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PROVIDER-SERVICE")
public interface ProviderClient {

    @GetMapping("/services/{id}")
    String getServiceById(@PathVariable("id") Long id);
}