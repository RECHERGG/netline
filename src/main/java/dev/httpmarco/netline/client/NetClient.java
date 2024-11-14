package dev.httpmarco.netline.client;

import dev.httpmarco.netline.NetworkComponentState;
import dev.httpmarco.netline.channel.NetChannel;
import dev.httpmarco.netline.channel.NetChannelInitializer;
import dev.httpmarco.netline.channel.NetChannelState;
import dev.httpmarco.netline.channel.NetClientHandler;
import dev.httpmarco.netline.impl.AbstractNetworkComponent;
import dev.httpmarco.netline.packet.BroadcastPacket;
import dev.httpmarco.netline.packet.ChannelIdentifyPacket;
import dev.httpmarco.netline.packet.Packet;
import dev.httpmarco.netline.request.BadRequestPacket;
import dev.httpmarco.netline.request.RequestPacket;
import dev.httpmarco.netline.request.ResponderRegisterPacket;
import dev.httpmarco.netline.request.ResponsePacket;
import dev.httpmarco.netline.utils.NetworkUtils;
import io.netty5.bootstrap.Bootstrap;
import io.netty5.channel.ChannelOption;
import io.netty5.channel.epoll.Epoll;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.function.Function;

@Accessors(fluent = true)
public final class NetClient extends AbstractNetworkComponent<NetClientConfig> {

    private final Bootstrap bootstrap;

    @Getter
    @Setter
    private NetChannel channel;

    public NetClient() {
        super(0, new NetClientConfig());

        this.bootstrap = new Bootstrap()
                .group(bossGroup())
                .channelFactory(NetworkUtils::createChannelFactory)
                .handler(new NetChannelInitializer(new NetClientHandler(this)))
                .option(ChannelOption.AUTO_READ, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.IP_TOS, 24)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);


        if (!config().disableTcpFastOpen() && Epoll.isTcpFastOpenClientSideAvailable()) {
            bootstrap.option(ChannelOption.TCP_FASTOPEN_CONNECT, true);
        }

        track(ChannelIdentifyPacket.class, (channel, packet) -> {

            if (!this.channel.equals(channel)) {
                channel.close();
                return;
            }

            channel.id(packet.id());
            channel.state(NetChannelState.READY);
        });
    }

    @Override
    public void boot() {
        this.bootstrap.connect(config().hostname(), config().port()).addListener(handleConnectionRelease());
    }

    @Override
    public void responderOf(String id, Function<Void, Packet> responder) {
        super.responderOf(id, responder);

        if (this.state() == NetworkComponentState.CONNECTION_ESTABLISHED && channel.state() == NetChannelState.READY) {
            this.channel.send(new ResponderRegisterPacket(id));
        }
    }

    public void broadcast(Packet packet) {
        this.channel.send(new BroadcastPacket(packet));
    }

    public void send(Packet packet) {
        this.channel.send(packet);
    }
}