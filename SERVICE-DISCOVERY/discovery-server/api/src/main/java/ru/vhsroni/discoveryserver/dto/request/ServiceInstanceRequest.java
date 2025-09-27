package ru.vhsroni.discoveryserver.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ServiceInstanceRequest(

        @Schema(description = "Logical name of the service to register", example = "user-service")
        @NotBlank(message = "Service name must not be blank")
        String serviceName,

        @Schema(description = "Port of the service instance", example = "8081")
        @Min(value = 1, message = "Port must be greater than 0")
        int port
) {}