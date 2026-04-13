package com.gl.project.BookingService.client;

import com.gl.project.BookingService.dto.ServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "PROVIDER-SERVICE")
public interface ProviderClient {

    @GetMapping("/api/services/{id}")
    ServiceDTO getServiceById(@PathVariable("id") Long id);

    @GetMapping("/api/services/provider/{providerId}")
    List<ServiceDTO> getServicesByProvider(@PathVariable("providerId") Long providerId);
}