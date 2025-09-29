package ru.vhsroni.discoverystarter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.vhsroni.discoverystarter.properties.DiscoveryProperties;

@Slf4j
@RequiredArgsConstructor
public class ServiceRegister {

    private final DiscoveryClient discoveryClient;

    private final DiscoveryProperties properties;


    @EventListener(ApplicationReadyEvent.class)
    public void registerService() {
        try {
            log.info("Registering service {}:{} with discovery server {}",
                    properties.getServiceName(),
                    properties.getServicePort(),
                    properties.getServerUrl());

            discoveryClient.register(properties.getServiceName(), properties.getServicePort());

            log.info("Successfully registered service {} with discovery server",
                    properties.getServiceName());
        } catch (Exception e) {
            log.error("Failed to register service {} with discovery server: {}",
                    properties.getServiceName(), e.getMessage());
        }
    }
}
