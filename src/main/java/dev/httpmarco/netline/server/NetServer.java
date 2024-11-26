package dev.httpmarco.netline.server;

import dev.httpmarco.netline.NetChannel;
import dev.httpmarco.netline.NetCompHandler;
import dev.httpmarco.netline.channel.NetClientChannel;
import dev.httpmarco.netline.packet.Packet;
import dev.httpmarco.netline.packet.common.ChannelIdentifyPacket;
import dev.httpmarco.netline.request.ResponderRegisterPacket;
import dev.httpmarco.netline.security.SecurityHandler;
import dev.httpmarco.netline.tracking.server.ClientConnectedTracking;
import io.netty5.channel.Channel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

@Getter
@Accessors(fluent = true)
public final class NetServer extends AbstractNetServer<NetServerConfig> {
    @NotNull
    private NetServerState state = NetServerState.INITIALIZE;
    @NotNull
    private final List<NetClientChannel> clients = new CopyOnWriteArrayList<>();
    @Nullable
    private SecurityHandler securityHandler;

    public NetServer() {
        super(new NetServerConfig());

        responderOf("channel_identification", (channel, properties) -> {
            this.clients.add((NetClientChannel) channel);
            callTracking(channel, new ClientConnectedTracking(channel));
            return new ChannelIdentifyPacket(channel.id());
        });

        track(ResponderRegisterPacket.class, (channel, packet) -> responders().put(packet.id(), (channel1, stringStringMap) -> channel.request(packet.id(), Packet.class).sync()));
    }

    @Override
    public NetChannel findChannel(Channel channel) {
        return this.clients.stream().filter(it -> it.equals(channel)).findFirst().orElse(null);
    }

    @Contract("_, _ -> new")
    @Override
    public @NotNull NetChannel generateChannel(Channel channel, @Nullable String id) {
        return new NetClientChannel(this, id, channel);
    }

    public void send(String id, Packet packet) {
        this.clients.stream().filter(it -> it.id().equals(id)).findFirst().ifPresent(channel -> channel.send(packet));
    }

    public void send(Predicate<NetClientChannel> predicate, Packet packet) {
        this.clients.stream().filter(predicate).forEach(channel -> channel.send(packet));
    }
    @Override
    public CompletableFuture<Void> onClose() {
        var future = CompletableFuture.allOf(this.clients.stream().map(NetClientChannel::close).toArray(CompletableFuture[]::new));
        this.state = NetServerState.CLOSED;
        return future;
    }

    @Override
    public void onBindFail(Throwable throwable) {
        this.state = NetServerState.FAILED;
    }

    @Override
    public void onBindSuccess() {
        this.state = NetServerState.BOOTED;
    }

    @Override
    public NetCompHandler handler() {
        return new NetServerHandler(this);
    }

    public void withSecurityPolicy(SecurityHandler securityHandler) {
        this.securityHandler = securityHandler;
    }

    public boolean hasSecurityPolicy() {
        return this.securityHandler != null;
    }
}
