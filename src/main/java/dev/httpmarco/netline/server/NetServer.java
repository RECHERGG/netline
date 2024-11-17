package dev.httpmarco.netline.server;

import dev.httpmarco.netline.NetworkComponentState;
import dev.httpmarco.netline.channel.NetChannelInitializer;
import dev.httpmarco.netline.channel.NetChannel;
import dev.httpmarco.netline.channel.NetChannelState;
import dev.httpmarco.netline.impl.AbstractNetworkComponent;
import dev.httpmarco.netline.packet.Packet;
import dev.httpmarco.netline.utils.NetworkUtils;
import io.netty5.bootstrap.ServerBootstrap;
import io.netty5.channel.ChannelOption;
import io.netty5.channel.EventLoopGroup;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

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
    public CompletableFuture<Void> boot() {
        var future = new CompletableFuture<Void>();
        state(NetworkComponentState.CONNECTING);

        var bootstrap = new ServerBootstrap()
                .group(bossGroup(), workerGroup)
                .channelFactory(NetworkUtils.generateChannelFactory())
                .childHandler(new NetChannelInitializer(new NetServerHandler(this)))
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.IP_TOS, 24)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        bootstrap.bind(config().hostname(), config().port()).addListener(handleConnectionRelease()).addListener(it -> future.complete(null));
        return future;
    }

    public void broadcast(Packet packet) {
        this.channels.stream().filter(it -> it.state() == NetChannelState.READY).forEach(channel -> channel.send(packet));
    }

    public void send(String id, Packet packet) {
        // todo need junit test
        this.send(channel -> channel.id().equals(id), packet);
    }

    public void send(Predicate<NetChannel> predicate, Packet packet) {
        // todo need junit test
        this.channels.stream().filter(predicate).forEach(channel -> channel.send(packet));
    }
}
