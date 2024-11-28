package dev.httpmarco.netline.node;

import dev.httpmarco.netline.NetChannel;
import dev.httpmarco.netline.NetCompHandler;
import dev.httpmarco.netline.client.NetClient;
import dev.httpmarco.netline.server.AbstractNetServer;
import io.netty5.channel.Channel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
@Accessors(fluent = true)
public final class NetNode extends AbstractNetServer<NetNodeConfig> {

    private final List<NetClient> clients = new ArrayList<>();
    private NetNodeState state = NetNodeState.INITIALIZING;

    public NetNode() {
        super(new NetNodeConfig());
    }

    @Contract(pure = true)
    @Override
    public @Nullable CompletableFuture<Void> onClose() {
        return null;
    }

    @Override
    public void onBindFail(Throwable throwable) {

    }

    @Override
    public CompletableFuture<Void> onBindSuccess() {
        var future = new CompletableFuture<Void>();
        this.state = NetNodeState.BIND_CLUSTER;

        if(this.config().bindings().isEmpty()) {
            // we are the only node
            this.state = NetNodeState.READY;
            return CompletableFuture.completedFuture(null);
        } else {
            CompletableFuture.allOf(config().bindings().stream().map(binding -> {
                var netNodeClient = new NetNodeClient(binding);
                clients.add(netNodeClient);
                return netNodeClient.boot();
            }).toArray(CompletableFuture[]::new)).whenComplete((unused, throwable) -> {
                this.state = NetNodeState.SYNC_CLUSTER;
                // todo

                this.state = NetNodeState.READY;
                future.complete(null);
            });
        }
        return future;
    }

    @Contract(pure = true)
    @Override
    public @Nullable NetCompHandler handler() {
        return null;
    }

    @Contract(pure = true)
    @Override
    public @Nullable NetChannel findChannel(Channel channel) {
        return null;
    }

    @Contract(pure = true)
    @Override
    public @Nullable NetChannel generateChannel(Channel channel, @Nullable String id) {
        return null;
    }
}
