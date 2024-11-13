package dev.httpmarco.netline.server;

import dev.httpmarco.netline.NetworkComponentHandler;
import dev.httpmarco.netline.channel.NetChannel;
import dev.httpmarco.netline.channel.NetChannelState;
import dev.httpmarco.netline.packet.ChannelIdentifyPacket;
import dev.httpmarco.netline.tracking.VerifiedChannelActiveTracking;
import io.netty5.channel.Channel;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class NetServerHandler extends NetworkComponentHandler {

    private final NetServer server;

    public NetServerHandler(NetServer server) {
        super(server);
        this.server = server;

        this.server.track(ChannelIdentifyPacket.class, (channel, it) -> {
            if (channel.state() != NetChannelState.ID_PENDING) {
                channel.close();
                return;
            }

            if (it.id() != null && server.channels().stream().anyMatch(s -> s.id() != null && s.id().equals(it.id()))) {
                log.warn("Duplicate channel name detected: {}", it.id());
                log.warn("Channel names must be unique!");

                channel.close();
                return;
            }

            channel.id(it.id());
            channel.state(NetChannelState.READY);

            // response a success with the id
            channel.send(it);

            this.server.callTracking(channel, new VerifiedChannelActiveTracking(channel));
        });
    }

    @Override
    public NetChannel findChannel(Channel channel) {
        return server.channels().stream().filter(it -> it.verifyChannel(channel)).findFirst().orElse(null);
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
