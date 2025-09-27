package ru.vhsroni.discoveryserver.service;

import ru.vhsroni.discoveryserver.domain.ServiceInstance;

import java.util.List;
import java.util.Optional;

public interface ServiceRegistry {

    void registerInstance(ServiceInstance instance);

    void removeInstance(ServiceInstance instance);

    Optional<ServiceInstance> getNextInstance();

    List<ServiceInstance> getInstances();
}
