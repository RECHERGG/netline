package dev.httpmarco.netline.channel;

import dev.httpmarco.netline.NetworkComponentHandler;
import io.netty5.channel.Channel;

public class NetClientHandler extends NetworkComponentHandler {

    @Override
    public NetChannel findChannel(Channel channel) {
        // todo
        return null;
    }

    @Override
    public void handshakeChannel(NetChannel netChannel) {
        // todo
    }

    @Override
    public void closeChannel(NetChannel netChannel) {
        // todo
    }
}
