package dev.httpmarco.netline.channel;

import dev.httpmarco.netline.NetChannel;
import dev.httpmarco.netline.packet.Packet;
import dev.httpmarco.netline.request.Request;
import io.netty5.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CompletableFuture;

@Log4j2
@Accessors(fluent = true)
@AllArgsConstructor
public class NetClientChannel implements NetChannel {

    @Getter
    private final String id;
    private Channel channel;

    @Override
    public void send(Packet packet) {
        if(channel == null || !this.channel.isActive()) {
            log.warn("Channel is not active, packet {} will not be sent on client with id {}", packet, "TODO");
            return;
        }
        this.channel.writeAndFlush(packet);
    }

    @Override
    public <T extends Packet> Request<T> request(String id, Class<T> packetClazz) {
        return new Request<>(this, id, packetClazz);
    }

    @Override
    public String hostname() {
        return channel.remoteAddress().toString().split(":", -1)[0].substring(1);
    }

    @Override
    public CompletableFuture<Void> close() {
        if(channel == null) {
            return CompletableFuture.completedFuture(null);
        }

        var future = new CompletableFuture<Void>();
        this.channel.close().addListener(it -> future.complete(null));
        return future;
    }

    public boolean equals(Channel channel) {
        return this.channel != null && this.channel.equals(channel);
    }

    public boolean ready() {
        return this.channel != null && this.channel.isActive();
    }
}
