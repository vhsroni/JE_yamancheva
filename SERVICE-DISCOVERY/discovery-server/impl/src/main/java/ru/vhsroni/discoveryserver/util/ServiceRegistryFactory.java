package ru.vhsroni.discoveryserver.util;

import ru.vhsroni.discoveryserver.service.ServiceRegistry;
import ru.vhsroni.discoveryserver.service.impl.ListServiceRegistry;

public class ServiceRegistryFactory {

    public static ServiceRegistry create() {
        return new ListServiceRegistry();
    }
}
