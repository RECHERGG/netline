package dev.httpmarco.netline.server;

import dev.httpmarco.netline.NetworkComponentState;
import dev.httpmarco.netline.channel.NetChannelInitializer;
import dev.httpmarco.netline.channel.NetChannel;
import dev.httpmarco.netline.impl.AbstractNetworkComponent;
import dev.httpmarco.netline.packet.ChannelIdentifyPacket;
import dev.httpmarco.netline.utils.NetworkUtils;
import io.netty5.bootstrap.ServerBootstrap;
import io.netty5.channel.EventLoopGroup;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
public class NetServer extends AbstractNetworkComponent<NetServerConfig> {

    private static final int BOSS_GROUP_THREADS = 1;

    private final EventLoopGroup workerGroup = NetworkUtils.createEventLoopGroup(0);

    @Getter
    private final List<NetChannel> channels = new ArrayList<>();

    public NetServer() {
        super(BOSS_GROUP_THREADS, new NetServerConfig());
    }

    @Override
    public void boot() {
        state(NetworkComponentState.CONNECTING);

        var bootstrap = new ServerBootstrap()
                .group(bossGroup(), workerGroup)
                .channelFactory(NetworkUtils.generateChannelFactory())
                .childHandler(new NetChannelInitializer(new NetServerHandler(this)));

        bootstrap.bind(config().hostname(), config().port()).addListener(handleConnectionRelease());
    }
}
