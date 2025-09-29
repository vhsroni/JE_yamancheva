package ru.vhsroni.discoveryserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.vhsroni.discoveryserver.api.DiscoveryServerApi;
import ru.vhsroni.discoveryserver.service.DiscoveryRegistry;
import ru.vhsroni.discoveryserver.domain.ServiceInstance;
import ru.vhsroni.discoveryserver.dto.request.ServiceInstanceRequest;
import ru.vhsroni.discoveryserver.dto.response.RegisterResponse;

import java.util.Optional;

@RestController
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
    public ResponseEntity<String> discoverService(String serviceName) {
        Optional<ServiceInstance> instance = discoveryRegistry.discover(serviceName);
        return instance.map(i -> ResponseEntity.ok(i.getInstanceUrl()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
