package ru.vhsroni.discoverystarter.client;

import org.springframework.web.reactive.function.client.WebClient;

public class DiscoveryClient {
    private final String serverUrl;
    private final WebClient webClient;

    public DiscoveryClient(String serverUrl, WebClient.Builder webClientBuilder) {
        this.serverUrl = serverUrl;
        this.webClient = webClientBuilder.baseUrl(serverUrl).build();
    }

    public String getInstance(String serviceName) {
        return webClient.get()
                .uri("/discover/{serviceName}", serviceName)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void register(String serviceName, int servicePort) {
        RegistrationRequest body = new RegistrationRequest(serviceName, servicePort);
        webClient.post()
                .uri("/register")
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public record RegistrationRequest(String serviceName, int servicePort) {}
}

