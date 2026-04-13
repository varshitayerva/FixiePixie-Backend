package com.gl.project.ProviderService.dto;

import com.gl.project.ProviderService.entity.ServiceCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ServiceDTO {

    private Long id;

    @NotBlank(message = "Service name is required")
    private String serviceName;

    @Positive(message = "Price must be greater than 0")
    private double price;

    // NEW FIELD
    @NotBlank(message = "Description is required")
    private String description;

    // ENUM FIELD
    private ServiceCategory category;

    private Long providerId;

    public ServiceDTO() {}

    public ServiceDTO(Long id, String serviceName, double price,
                      String description, ServiceCategory category, Long providerId) {
        this.id = id;
        this.serviceName = serviceName;
        this.price = price;
        this.description = description;
        this.category = category;
        this.providerId = providerId;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public void setCategory(ServiceCategory category) {
        this.category = category;
    }
    public Long getProviderId() { return providerId; }
    public void setProviderId(Long providerId) { this.providerId = providerId; }

    @Override
    public String toString() {
        return "ServiceDTO{" +
                "id=" + id +
                ", serviceName='" + serviceName + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", category=" + category +
                '}';
    }
}