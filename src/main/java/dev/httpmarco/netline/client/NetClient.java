package dev.httpmarco.netline.client;

import dev.httpmarco.netline.channel.ChannelInitializer;
import dev.httpmarco.netline.impl.AbstractNetworkComponent;
import dev.httpmarco.netline.utils.NetworkUtils;
import io.netty5.bootstrap.Bootstrap;

public final class NetClient extends AbstractNetworkComponent<NetClientConfig> {

    private final Bootstrap bootstrap;

    public NetClient() {
        super(0, new NetClientConfig());

        this.bootstrap = new Bootstrap()
                .group(bossGroup())
                .channelFactory(NetworkUtils::createChannelFactory)
                .handler(new ChannelInitializer());
    }

    @Override
    public void boot() {
        this.bootstrap.connect(config().hostname(), config().port()).addListener(handleConnectionRelease());
    }
}
