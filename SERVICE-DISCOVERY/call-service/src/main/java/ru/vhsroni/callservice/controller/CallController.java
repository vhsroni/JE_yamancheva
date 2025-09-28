package ru.vhsroni.callservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.vhsroni.discoverystarter.client.DiscoveryClient;

@RestController
@RequiredArgsConstructor
public class CallController {

    private final DiscoveryClient discoveryClient;

    private final WebClient webClient = WebClient.builder().build();

    @GetMapping("/call-user")
    public Mono<String> callUser() {
        String instance = discoveryClient.getInstance("user-service");
        String base = "http://" + instance;
        return webClient.get().uri(base + "/hello").retrieve().bodyToMono(String.class);
    }
}
