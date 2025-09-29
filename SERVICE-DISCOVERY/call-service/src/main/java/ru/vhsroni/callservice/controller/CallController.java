package ru.vhsroni.callservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.vhsroni.discoverystarter.service.DiscoveryClient;


@RestController
@RequiredArgsConstructor
public class CallController {

    private final DiscoveryClient discoveryClient;

    private final WebClient webClient;

    @GetMapping("/call-user")
    public Mono<String> callUser() {
        String instanceBaseUrl = discoveryClient.getInstance("user-service");
        return webClient.get().uri(instanceBaseUrl + "/hello").retrieve().bodyToMono(String.class);
    }
}
