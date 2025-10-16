package com.rin.orderservice.client;

import com.rin.orderservice.config.FeignClientInterceptorConfig;
import com.rin.orderservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", configuration = FeignClientInterceptorConfig.class)
public interface UserClient {
    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable("id") Long id);

    @GetMapping("/api/users/keycloak/{sub}")
    UserDto getUserByKeycloakId(@PathVariable("sub") String keycloakId);
}
