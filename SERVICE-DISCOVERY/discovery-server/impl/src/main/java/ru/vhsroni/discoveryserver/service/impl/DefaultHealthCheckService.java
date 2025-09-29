package ru.vhsroni.discoveryserver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.vhsroni.discoveryserver.service.DiscoveryRegistry;
import ru.vhsroni.discoveryserver.domain.InstanceStatus;
import ru.vhsroni.discoveryserver.service.HealthCheckService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultHealthCheckService implements HealthCheckService {

    private final DiscoveryRegistry discoveryRegistry;

    private final WebClient webClient;

    @Override
    @Scheduled(fixedRate = 10_000)
    public void checkServices() {
        log.info("health-check-service begin");

        List<Mono<Void>> healthChecks = new ArrayList<>();

        discoveryRegistry.getAll().values().forEach(registryList ->
                registryList.getInstances().forEach(instance -> {
                    Mono<Void> healthCheck = webClient.get()
                            .uri(instance.getInstanceUrl() + "/health")
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnSuccess(resp -> {
                                instance.setStatus(InstanceStatus.UP);
                                instance.setLastHeartbeat(Instant.now());
                                log.debug("Service {} is UP", instance.getInstanceUrl());
                            })
                            .doOnError(err -> {
                                instance.setStatus(InstanceStatus.DOWN);
                                log.warn("Service {} is DOWN: {}", instance.getInstanceUrl(), err.getMessage());
                            })
                            .then();

                    healthChecks.add(healthCheck);
                })
        );

        Mono.when(healthChecks)
                .doOnTerminate(() -> {
                    discoveryRegistry.cleanup();
                    log.info("health-check-service completed");
                })
                .subscribe();
    }
}
