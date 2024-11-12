package dev.httpmarco.netline.client;

import dev.httpmarco.netline.config.NetworkConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(fluent = true)
public final class NetClientConfig extends NetworkConfig {

    private String id = UUID.randomUUID().toString();
    private boolean disableTcpFastOpen = false;

    public NetClientConfig() {
        super("0.0.0.0", 9091);
    }
}
