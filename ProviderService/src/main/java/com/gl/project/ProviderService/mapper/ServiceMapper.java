package com.gl.project.ProviderService.mapper;

import com.gl.project.ProviderService.dto.ServiceDTO;
import com.gl.project.ProviderService.entity.ProviderService;


public class ServiceMapper {
    public static ProviderService toEntity(ServiceDTO dto)
    {
        if(dto == null)
        {
            return null;
        }
        ProviderService service = new ProviderService();
        service.setId(dto.getId());
        service.setServiceName(dto.getServiceName());
        service.setPrice(dto.getPrice());

        return service;
    }

    public static ServiceDTO toDTO(ProviderService service)
    {
        if(service == null)
        {
            return null;
        }

        ServiceDTO dto = new ServiceDTO();
        dto.setId(service.getId());
        dto.setServiceName(service.getServiceName());
        dto.setPrice(service.getPrice());
        return dto;

    }
}
