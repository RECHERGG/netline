package dev.httpmarco.netline.channel;

import dev.httpmarco.netline.packet.Packet;
import io.netty5.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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

    public String hostname() {
        return this.channel.remoteAddress().toString().split(":", -1)[0].substring(1);
    }
}
