package ru.vhsroni.discoverystarter.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(DiscoveryAutoConfig.class);

    private final DiscoveryProperties properties;

    public DiscoveryAutoConfig(DiscoveryProperties properties) {
        this.properties = properties;
        log.info("DiscoveryAutoConfig initialized with server URL: {}", properties.getServerUrl());
    }

    @Bean
    @ConditionalOnMissingBean(WebClient.Builder.class)
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @ConditionalOnMissingBean(DiscoveryClient.class)
    public DiscoveryClient discoveryClient(WebClient.Builder webClientBuilder) {
        log.info("Creating DiscoveryClient bean for server: {}", properties.getServerUrl());
        return new DiscoveryClient(properties.getServerUrl(), webClientBuilder);
    }

    @PostConstruct
    public void init() {
        log.info("Discovery starter configured for service: {}:{}",
                properties.getServiceName(), properties.getServicePort());
    }
}