package dev.httpmarco.netline.channel;

import dev.httpmarco.netline.packet.Packet;
import dev.httpmarco.netline.request.RequestPacket;
import dev.httpmarco.netline.request.RequestRegister;
import io.netty5.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Accessors(fluent = true)
public final class NetChannel {

    @Setter
    @Getter
    private String id;
    private final Channel channel;

    @Setter
    @Getter
    private NetChannelState state = NetChannelState.ID_PENDING;

    public NetChannel(Channel channel) {
        this.channel = channel;
    }

    public void send(Packet packet) {
        channel.writeAndFlush(packet);
    }

    public void close() {
        this.channel.close();
    }

    public boolean verifyChannel(Channel channel) {
        return this.channel.equals(channel);
    }

    public @NotNull String hostname() {
        return this.channel.remoteAddress().toString().split(":", -1)[0].substring(1);
    }

    @SneakyThrows
    public <T extends Packet> T request(String id, Class<T> packet) {
        return requestAsync(id, packet).get(5, TimeUnit.SECONDS);
    }

    public <T extends Packet> CompletableFuture<T> requestAsync(String id, Class<T> packet) {
        var future = new CompletableFuture<T>();
        var requestId = UUID.randomUUID();

        RequestRegister.register(requestId, future);
        this.channel.writeAndFlush(new RequestPacket(requestId, id));
        return future;
    }
}
