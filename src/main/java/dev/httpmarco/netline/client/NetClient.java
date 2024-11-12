package dev.httpmarco.netline.client;

import dev.httpmarco.netline.channel.NetChannel;
import dev.httpmarco.netline.channel.NetChannelInitializer;
import dev.httpmarco.netline.channel.NetClientHandler;
import dev.httpmarco.netline.impl.AbstractNetworkComponent;
import dev.httpmarco.netline.utils.NetworkUtils;
import io.netty5.bootstrap.Bootstrap;
import io.netty5.channel.ChannelOption;
import io.netty5.channel.epoll.Epoll;

public final class NetClient extends AbstractNetworkComponent<NetClientConfig> {

    private final Bootstrap bootstrap;
    private NetChannel channel;

    public NetClient() {
        super(0, new NetClientConfig());

        this.bootstrap = new Bootstrap()
                .group(bossGroup())
                .channelFactory(NetworkUtils::createChannelFactory)
                .handler(new NetChannelInitializer(new NetClientHandler()));

        if(!config().disableTcpFastOpen() && Epoll.isTcpFastOpenClientSideAvailable()) {
            bootstrap.option(ChannelOption.TCP_FASTOPEN_CONNECT, true);
        }
    }

    @Override
    public void boot() {
        this.bootstrap.connect(config().hostname(), config().port()).addListener(handleConnectionRelease());
    }
}