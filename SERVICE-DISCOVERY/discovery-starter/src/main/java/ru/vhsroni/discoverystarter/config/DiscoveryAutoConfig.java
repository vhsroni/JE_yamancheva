package ru.vhsroni.discoverystarter.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.vhsroni.discoverystarter.client.DiscoveryClient;
import ru.vhsroni.discoverystarter.properties.DiscoveryProperties;

@Configuration
@EnableConfigurationProperties(DiscoveryProperties.class)
public class DiscoveryAutoConfig {

    private final DiscoveryProperties properties;
    private final WebClient.Builder webClientBuilder;

    public DiscoveryAutoConfig(DiscoveryProperties properties, WebClient.Builder webClientBuilder) {
        this.properties = properties;
        this.webClientBuilder = webClientBuilder;
    }

    @PostConstruct
    public void register() {
        discoveryClient().register(properties.getServiceName(), properties.getServicePort());
    }

    @Bean
    @ConditionalOnMissingBean
    public DiscoveryClient discoveryClient() {
        return new DiscoveryClient(properties.getServerUrl(), webClientBuilder);
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
