package com.gl.project.ProviderService.entity;

import jakarta.persistence.*;

@Entity
@Table(name ="services")
public class ProviderService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="service_name", nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private double price;


    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Long providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceCategory category;


    public ProviderService() {}

    public ProviderService(Long id, String serviceName, double price,
                           String description, ServiceCategory category,Long providerId) {
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

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }
}

