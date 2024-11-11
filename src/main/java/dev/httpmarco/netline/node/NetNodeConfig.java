package dev.httpmarco.netline.node;

import dev.httpmarco.netline.config.NetworkConfig;

public final class NetNodeConfig extends NetworkConfig {

    public NetNodeConfig(int port) {
        super("0.0.0.0", 9091);
    }
}
