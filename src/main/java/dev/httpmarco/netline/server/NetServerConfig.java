package dev.httpmarco.netline.server;

import dev.httpmarco.netline.config.NetworkConfig;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
public final class NetServerConfig extends NetworkConfig {

    public NetServerConfig() {
        super("0.0.0.0", 9091);
    }
}
