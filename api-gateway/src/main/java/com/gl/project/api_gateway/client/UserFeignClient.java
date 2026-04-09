package com.gl.project.api_gateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "USER-SERVICE", url = "http://localhost:2002")
public interface UserFeignClient {

    @PostMapping("/auth/login")
    Object login(@RequestBody Object request);
}