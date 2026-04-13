package com.gl.project.ProviderService.controller;

import com.gl.project.ProviderService.dto.ServiceDTO;
import com.gl.project.ProviderService.entity.ServiceCategory;
import com.gl.project.ProviderService.service.ServiceServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceServiceInterface service;


    @PostMapping
    public ServiceDTO addService(@Valid @RequestBody ServiceDTO dto) {
        return service.addService(dto);
    }


    @GetMapping("/{id}")
    public ServiceDTO getServiceById(@PathVariable Long id) {
        return service.getServiceById(id);
    }


    @GetMapping
    public List<ServiceDTO> getAllServices() {
        return service.getAllServices();
    }


    @PutMapping("/{id}")
    public ServiceDTO updateService(@PathVariable Long id,
                                    @Valid @RequestBody ServiceDTO dto) {
        return service.updateService(id, dto);
    }

    @GetMapping("/category/{category}")
    public List<ServiceDTO> getByCategory(@PathVariable ServiceCategory category) {
        return service.getByCategory(category);
    }


    @DeleteMapping("/{id}")
    public String deleteService(@PathVariable Long id) {
        service.deleteService(id);
        return "Service deleted successfully with ID: " + id;
    }

    @GetMapping("/provider/{providerId}")
    public List<ServiceDTO> getServicesByProvider(@PathVariable Long providerId) {
        return service.getServicesByProvider(providerId);
        // Note: You'll need to add this method to your Interface and Impl too
    }
}