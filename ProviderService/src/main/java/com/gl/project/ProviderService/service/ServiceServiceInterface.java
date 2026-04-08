package com.gl.project.ProviderService.service;

import com.gl.project.ProviderService.dto.ServiceDTO;
import com.gl.project.ProviderService.entity.ServiceCategory;

import java.util.List;

public interface ServiceServiceInterface {


    ServiceDTO addService(ServiceDTO dto);


    ServiceDTO getServiceById(Long id);


    List<ServiceDTO> getAllServices();

    ServiceDTO updateService(Long id, ServiceDTO dto);

    List<ServiceDTO> getByCategory(ServiceCategory category);



    void deleteService(Long id);
}
