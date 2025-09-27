package ru.vhsroni.discoveryserver.service.impl;


import org.springframework.stereotype.Component;
import ru.vhsroni.discoveryserver.domain.InstanceStatus;
import ru.vhsroni.discoveryserver.domain.ServiceInstance;
import ru.vhsroni.discoveryserver.service.DiscoveryRegistry;
import ru.vhsroni.discoveryserver.service.ServiceRegistry;
import ru.vhsroni.discoveryserver.util.ServiceRegistryFactory;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MapDiscoveryRegistry implements DiscoveryRegistry {

    private final ConcurrentHashMap<String, ServiceRegistry> services = new ConcurrentHashMap<>();

    public void register(ServiceInstance instance) {
        services
                .computeIfAbsent(instance.getServiceName(), key -> ServiceRegistryFactory.create())
                .registerInstance(instance);
    }

    public Optional<ServiceInstance> discover(String serviceName) {
        ServiceRegistry registry = services.get(serviceName);
        if (registry == null) return Optional.empty();
        return registry.getNextInstance();
    }

    public void cleanup() {
        services.values().forEach(registry ->
                registry.getInstances().stream()
                        .filter(i -> i.getStatus() == InstanceStatus.DOWN)
                        .forEach(registry::removeInstance)
        );
    }
    public ConcurrentHashMap<String, ServiceRegistry> getAll() {
        return services;
    }
}
