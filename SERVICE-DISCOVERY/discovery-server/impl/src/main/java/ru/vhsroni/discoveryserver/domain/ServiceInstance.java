package ru.vhsroni.discoveryserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class ServiceInstance {

    private final String serviceName;

    private final String host;

    private final int port;

    private Instant lastHeartbeat;

    private InstanceStatus status;

    public String getInstanceUrl() {
        return "http://" + host + ":" + port;
    }

    public void setLastHeartbeat(Instant lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public void setStatus(InstanceStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceInstance that)) return false;
        return port == that.port &&
               serviceName.equals(that.serviceName) &&
               host.equals(that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, host, port);
    }
}
