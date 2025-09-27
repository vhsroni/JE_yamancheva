package ru.vhsroni.discoveryserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import ru.vhsroni.discoveryserver.api.DiscoveryServerApi;
import ru.vhsroni.discoveryserver.service.DiscoveryRegistry;
import ru.vhsroni.discoveryserver.domain.ServiceInstance;
import ru.vhsroni.discoveryserver.dto.request.ServiceInstanceRequest;
import ru.vhsroni.discoveryserver.dto.response.RegisterResponse;
import ru.vhsroni.discoveryserver.dto.response.ServiceInstanceResponse;

import java.util.Optional;

@RequiredArgsConstructor
public class DiscoveryServerController implements DiscoveryServerApi {

    private final DiscoveryRegistry discoveryRegistry;

    @Override
    public ResponseEntity<RegisterResponse> registerService(ServiceInstanceRequest request, String forwardedFor, HttpServletRequest httpRequest) {
        String host = forwardedFor != null ? forwardedFor : httpRequest.getRemoteAddr();
        ServiceInstance instance = new ServiceInstance(request.serviceName(), host, request.port());
        discoveryRegistry.register(instance);
        return ResponseEntity.ok(new RegisterResponse("registered"));
    }

    @Override
    public ResponseEntity<ServiceInstanceResponse> discoverService(String serviceName) {
        Optional<ServiceInstance> instance = discoveryRegistry.discover(serviceName);
        return instance.map(i -> ResponseEntity.ok(new ServiceInstanceResponse(i.getInstanceUrl())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
