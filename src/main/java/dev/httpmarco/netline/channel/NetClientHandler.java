package dev.httpmarco.netline.channel;

import dev.httpmarco.netline.NetworkComponentHandler;
import dev.httpmarco.netline.client.NetClient;
import dev.httpmarco.netline.packet.ChannelIdentifyPacket;
import io.netty5.channel.Channel;
import org.jetbrains.annotations.NotNull;

public class NetClientHandler extends NetworkComponentHandler {

    private final NetClient client;

    public NetClientHandler(NetClient client) {
        super(client);
        this.client = client;
    }

    @Override
    public NetChannel findChannel(Channel channel) {
        // todo
        return null;
    }

    @Override
    public void handshakeChannel(@NotNull NetChannel netChannel) {
        netChannel.send(new ChannelIdentifyPacket(client.config().id()));
    }

    @Override
    public void closeChannel(NetChannel netChannel) {
        // todo
    }
}
