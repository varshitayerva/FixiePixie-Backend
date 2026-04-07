package com.gl.project.ProviderService.repository;

import com.gl.project.ProviderService.entity.ProviderService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<ProviderService, Long> {


    boolean existsByServiceName(String serviceName);
}
