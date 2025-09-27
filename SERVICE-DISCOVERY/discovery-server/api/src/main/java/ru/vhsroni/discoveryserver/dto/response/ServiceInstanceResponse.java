package ru.vhsroni.discoveryserver.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ServiceInstanceResponse(

        @Schema(
                description = "Base URL of the service instance. Must be accessible for health checks.",
                example = "http://localhost:8081"
        )
        String instanceUrl
) {
}
