package ru.vhsroni.discoverystarter.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "discovery")
public class DiscoveryProperties {

    private String serverUrl;

    private String serviceName;

    private int servicePort;
}