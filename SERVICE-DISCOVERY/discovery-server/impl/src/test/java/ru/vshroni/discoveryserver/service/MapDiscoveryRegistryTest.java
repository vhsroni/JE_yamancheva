package ru.vshroni.discoveryserver.service;

import org.junit.jupiter.api.Test;
import ru.vhsroni.discoveryserver.domain.ServiceInstance;
import ru.vhsroni.discoveryserver.service.impl.MapDiscoveryRegistry;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapDiscoveryRegistryTest {

    @Test
    void registerAndDiscover() {
        MapDiscoveryRegistry registry = new MapDiscoveryRegistry();
        ServiceInstance a = new ServiceInstance("users", "127.0.0.1", 8081);
        registry.register(a);
        Optional<ServiceInstance> found = registry.discover("users");
        assertTrue(found.isPresent());
        assertEquals(a.getServiceName(), found.get().getServiceName());
    }

    @Test
    void cleanupRemovesDown() {
        MapDiscoveryRegistry registry = new MapDiscoveryRegistry();
        ServiceInstance a = new ServiceInstance("users", "127.0.0.1", 8081);
        a.setStatus(ru.vhsroni.discoveryserver.domain.InstanceStatus.DOWN);
        registry.register(a);
        registry.cleanup();
        assertTrue(registry.getAll().get("users").getInstances().isEmpty());
    }
}