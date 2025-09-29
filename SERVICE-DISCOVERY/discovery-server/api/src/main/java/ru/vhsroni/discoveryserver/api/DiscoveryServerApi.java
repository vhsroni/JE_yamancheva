package ru.vhsroni.discoveryserver.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vhsroni.discoveryserver.dto.request.ServiceInstanceRequest;
import ru.vhsroni.discoveryserver.dto.response.ServiceInstanceResponse;
import ru.vhsroni.discoveryserver.dto.response.RegisterResponse;

@Tag(
        name = "Service Discovery API",
        description = "Registering and discovering service instances"
)
@RequestMapping("/api/v1/discovery")
public interface DiscoveryServerApi {

    @Operation(
            summary = "Register a new service instance",
            description = "A service reports its name and port. Host is automatically determined from the request."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Service instance successfully registered",
            content = @Content(schema = @Schema(implementation = RegisterResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content(schema = @Schema(hidden = true))
    )
    @PostMapping("/register")
    ResponseEntity<RegisterResponse> registerService(
            @Parameter(
                    description = "Information about the service instance being registered",
                    required = true
            )
            @Valid @RequestBody ServiceInstanceRequest request,
            @Parameter(hidden = true)
            @RequestHeader(value = "X-Forwarded-For", required = false) String forwardedFor,
            @Parameter(hidden = true)
            HttpServletRequest httpRequest
    );

    @Operation(
            summary = "Discover a service instance by name",
            description = "Round-robin implementation: returns the next available instance of the requested service."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Service instance successfully retrieved",
            content = @Content(schema = @Schema(implementation = ServiceInstanceResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Service with the specified name was not found",
            content = @Content(schema = @Schema(hidden = true))
    )
    @GetMapping("/discover/{serviceName}")
    ResponseEntity<String> discoverService(
            @Parameter(
                    description = "The logical name of the service being requested",
                    required = true,
                    example = "user-service"
            )
            @PathVariable String serviceName
    );
}
