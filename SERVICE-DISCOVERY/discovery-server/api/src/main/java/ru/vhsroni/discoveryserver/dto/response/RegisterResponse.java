package ru.vhsroni.discoveryserver.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterResponse(
        @Schema(description = "Status of the registration", example = "registered")
        String status
) {}
