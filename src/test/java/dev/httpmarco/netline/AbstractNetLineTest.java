package dev.httpmarco.netline;

import dev.httpmarco.netline.client.NetClient;
import dev.httpmarco.netline.server.NetServer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public abstract class AbstractNetLineTest {

    @Getter
    @Setter
    private static NetServer server;
    @Getter
    @Setter
    private static NetClient client;

}
