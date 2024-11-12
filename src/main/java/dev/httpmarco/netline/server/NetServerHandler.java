package dev.httpmarco.netline.server;

import dev.httpmarco.netline.NetworkComponentHandler;
import dev.httpmarco.netline.channel.NetChannel;
import io.netty5.channel.Channel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NetServerHandler extends NetworkComponentHandler {

    private final NetServer server;

    @Override
    public NetChannel findChannel(Channel channel) {
        return server.channels().stream().filter(c -> c.channel().equals(channel)).findFirst().orElse(null);
    }

    @Override
    public void handshakeChannel(NetChannel netChannel) {
        // todo maybe security stuff here
        this.server.channels().add(netChannel);
    }

    @Override
    public void closeChannel(NetChannel netChannel) {
        this.server.channels().remove(netChannel);
    }
}
