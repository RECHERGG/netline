package dev.httpmarco.netline.node;

import dev.httpmarco.netline.config.CompConfig;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class NetNodeConfig extends CompConfig {

    private List<NetNodeBinding> bindings = new ArrayList<>();

    public NetNodeConfig() {
        super("0.0.0.0", 9091, true, 5);
    }
}
