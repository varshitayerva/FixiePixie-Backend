package com.gl.project.ProviderService.utility;

public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException(String message) {

        super(message);
    }
}
