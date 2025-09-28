package ru.vshroni.discoveryserver.service;

import org.junit.jupiter.api.Test;
import ru.vhsroni.discoveryserver.domain.ServiceInstance;
import ru.vhsroni.discoveryserver.service.impl.ListServiceRegistry;

public class ListServiceRegistryTest {

    @Test
    void roundRobinReturnsNextInstance() {
        ListServiceRegistry registry = new ListServiceRegistry();
        ServiceInstance a = new ServiceInstance("svc", "host1", 8081);
        ServiceInstance b = new ServiceInstance("svc", "host2", 8082);
        registry.registerInstance(a);
        registry.registerInstance(b);

        assertEquals(a, registry.getNextInstance().orElseThrow());
        assertEquals(b, registry.getNextInstance().orElseThrow());
        assertEquals(a, registry.getNextInstance().orElseThrow());
    }

    @Test
    void removeInstanceRemovesProperly() {
        ListServiceRegistry registry = new ListServiceRegistry();
        ServiceInstance a = new ServiceInstance("svc", "host1", 8081);
        registry.registerInstance(a);
        assertTrue(registry.getNextInstance().isPresent());
        registry.removeInstance(a);
        assertTrue(registry.getInstances().isEmpty());
    }
}