package com.gl.project.ProviderService.service;

import com.gl.project.ProviderService.dto.ServiceDTO;

import java.util.List;

public interface ServiceServiceInterface {


    ServiceDTO addService(ServiceDTO dto);


    ServiceDTO getServiceById(Long id);


    List<ServiceDTO> getAllServices();

    ServiceDTO updateService(Long id, ServiceDTO dto);


    void deleteService(Long id);
}
