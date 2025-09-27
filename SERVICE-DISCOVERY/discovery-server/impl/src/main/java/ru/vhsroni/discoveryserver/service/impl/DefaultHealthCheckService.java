package ru.vhsroni.discoveryserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.vhsroni.discoveryserver.service.DiscoveryRegistry;
import ru.vhsroni.discoveryserver.domain.InstanceStatus;
import ru.vhsroni.discoveryserver.service.HealthCheckService;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class DefaultHealthCheckService implements HealthCheckService {

    private final DiscoveryRegistry discoveryRegistry;

    private final WebClient webClient;

    @Override
    @Scheduled(fixedRate = 10_000)
    public void checkServices() {
        discoveryRegistry.getAll().values().forEach(registryList ->
                registryList.getInstances().forEach(instance -> {
                    webClient.get()
                            .uri(instance.getInstanceUrl() + "/health")
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnSuccess(resp -> {
                                instance.setStatus(InstanceStatus.UP);
                                instance.setLastHeartbeat(Instant.now());
                            })
                            .doOnError(err -> instance.setStatus(InstanceStatus.DOWN))
                            .subscribe();
                })
        );

       discoveryRegistry.cleanup();
    }
}
