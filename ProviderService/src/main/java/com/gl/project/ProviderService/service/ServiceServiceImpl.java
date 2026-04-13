package com.gl.project.ProviderService.service;

import com.gl.project.ProviderService.dto.ServiceDTO;
import com.gl.project.ProviderService.entity.ProviderService;
import com.gl.project.ProviderService.entity.ServiceCategory;
import com.gl.project.ProviderService.utility.DuplicateServiceException;
import com.gl.project.ProviderService.utility.ServiceNotFoundException;
import com.gl.project.ProviderService.mapper.ServiceMapper;
import com.gl.project.ProviderService.repository.ServiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceServiceImpl implements ServiceServiceInterface {

    @Autowired
    private ServiceRepository repo;


    @Autowired
    private Environment env;


    @Override
    public ServiceDTO addService(ServiceDTO dto) {

        if (repo.existsByServiceName(dto.getServiceName())) {

            String msg = env.getProperty("ServiceService.DUPLICATE_SERVICE");

            throw new DuplicateServiceException(
                    msg + dto.getServiceName()
            );
        }

        ProviderService saved = repo.save(ServiceMapper.toEntity(dto));
        return ServiceMapper.toDTO(saved);
    }


    @Override
    public ServiceDTO getServiceById(Long id) {

        ProviderService service = repo.findById(id)
                .orElseThrow(() -> {
                    String msg = env.getProperty("ServiceService.SERVICE_NOT_FOUND");
                    return new ServiceNotFoundException(msg + id);
                });

        return ServiceMapper.toDTO(service);
    }

    @Override
    public List<ServiceDTO> getAllServices() {
        return repo.findAll()
                .stream()
                .map(ServiceMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ServiceDTO updateService(Long id, ServiceDTO dto) {

        ProviderService existing = repo.findById(id)
                .orElseThrow(() -> {
                    String msg = env.getProperty("ServiceService.SERVICE_NOT_FOUND");
                    return new ServiceNotFoundException(msg + id);
                });

        if (!existing.getServiceName().equals(dto.getServiceName()) &&
                repo.existsByServiceName(dto.getServiceName())) {

            String msg = env.getProperty("ServiceService.DUPLICATE_SERVICE");

            throw new DuplicateServiceException(
                    msg + dto.getServiceName()
            );
        }

        // update ALL fields
        existing.setServiceName(dto.getServiceName());
        existing.setPrice(dto.getPrice());
        existing.setDescription(dto.getDescription());
        existing.setCategory(dto.getCategory());

        existing.setProviderId(dto.getProviderId());

        return ServiceMapper.toDTO(repo.save(existing));
    }

    @Override
    public List<ServiceDTO> getByCategory(ServiceCategory category) {

        return repo.findByCategory(category)
                .stream()
                .map(ServiceMapper::toDTO)
                .toList();
    }

    @Override
    public void deleteService(Long id) {

        ProviderService service = repo.findById(id)
                .orElseThrow(() -> {
                    String msg = env.getProperty("ServiceService.SERVICE_NOT_FOUND");
                    return new ServiceNotFoundException(msg + id);
                });

        repo.delete(service);
    }

    @Override
    public List<ServiceDTO> getServicesByProvider(Long providerId) {
        return repo.findByProviderId(providerId)
                .stream()
                .map(ServiceMapper::toDTO)
                .collect(Collectors.toList());
    }
}