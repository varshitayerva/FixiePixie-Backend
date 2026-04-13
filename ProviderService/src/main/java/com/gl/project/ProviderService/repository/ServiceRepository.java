package com.gl.project.ProviderService.repository;

import com.gl.project.ProviderService.entity.ProviderService;
import com.gl.project.ProviderService.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ProviderService, Long> {
    List<ProviderService> findByCategory(ServiceCategory category);

    boolean existsByServiceName(String serviceName);

    List<ProviderService> findByProviderId(Long providerId);
}
