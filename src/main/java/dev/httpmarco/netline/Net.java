package dev.httpmarco.netline;

import dev.httpmarco.netline.client.NetClient;
import dev.httpmarco.netline.node.NetNode;
import dev.httpmarco.netline.server.NetServer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Net {

    @Contract(" -> new")
    public static @NotNull Net line() {
        return new Net();
    }

    @Contract(" -> new")
    public @NotNull NetClient client() {
        return new NetClient();
    }

    @Contract(" -> new")
    public @NotNull NetServer server() {
        return new NetServer();
    }

    @Contract(" -> new")
    public @NotNull NetNode node() {
        return new NetNode();
    }
}