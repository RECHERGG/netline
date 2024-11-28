package dev.httpmarco.netline.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class NetNodeBinding {

    private final String id;
    private final String hostname;
    private final int port;

}
