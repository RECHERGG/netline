package dev.httpmarco.netline.client;

import dev.httpmarco.netline.config.NetworkConfig;

public final class NetClientConfig extends NetworkConfig {

    public NetClientConfig() {
        super("0.0.0.0", 9091);
    }
}
