package dev.httpmarco.netline.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class NetworkConfig {

    private String hostname;
    private int port;

}
