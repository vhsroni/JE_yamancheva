package ru.vhsroni.discoveryserver.service;

import ru.vhsroni.discoveryserver.domain.ServiceInstance;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public interface DiscoveryRegistry {

    void register(ServiceInstance instance);

    public Optional<ServiceInstance> discover(String serviceName);

    public void cleanup();

    ConcurrentHashMap<String, ServiceRegistry> getAll();
}
