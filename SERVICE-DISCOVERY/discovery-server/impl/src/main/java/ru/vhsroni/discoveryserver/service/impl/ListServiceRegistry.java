package ru.vhsroni.discoveryserver.service.impl;


import ru.vhsroni.discoveryserver.domain.ServiceInstance;
import ru.vhsroni.discoveryserver.service.ServiceRegistry;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ListServiceRegistry implements ServiceRegistry {

    private final CopyOnWriteArrayList<ServiceInstance> instances = new CopyOnWriteArrayList<>();
    private final AtomicInteger roundRobinCounter = new AtomicInteger(0);

    @Override
    public void registerInstance(ServiceInstance instance) {
        if (!instances.contains(instance)) {
            instances.add(instance);
        } else {
            instances.stream()
                    .filter(i -> i.equals(instance))
                    .forEach(i -> i.setLastHeartbeat(instance.getLastHeartbeat()));
        }
    }

    @Override
    public void removeInstance(ServiceInstance instance) {
        instances.remove(instance);
    }

    @Override
    public Optional<ServiceInstance> getNextInstance() {
        if (instances.isEmpty()) return Optional.empty();
        int index = roundRobinCounter.getAndIncrement() % instances.size();
        return Optional.of(instances.get(index));
    }

    @Override
    public List<ServiceInstance> getInstances() {
        return Collections.unmodifiableList(instances);
    }
}
