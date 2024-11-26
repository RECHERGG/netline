package dev.httpmarco.netline.node;

import dev.httpmarco.netline.config.CompConfig;

public final class NetNodeConfig extends CompConfig {

    public NetNodeConfig() {
        super("0.0.0.0", 9091, true, 5);
    }
}
