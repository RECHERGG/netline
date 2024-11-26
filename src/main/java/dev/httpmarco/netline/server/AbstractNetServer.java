package dev.httpmarco.netline.server;

import dev.httpmarco.netline.NetChannel;
import dev.httpmarco.netline.NetCompHandler;
import dev.httpmarco.netline.channel.NetChannelInitializer;
import dev.httpmarco.netline.config.CompConfig;
import dev.httpmarco.netline.impl.AbstractNetCompImpl;
import dev.httpmarco.netline.utils.NetworkUtils;
import io.netty5.bootstrap.ServerBootstrap;
import io.netty5.channel.ChannelOption;
import io.netty5.channel.EventLoopGroup;
import io.netty5.channel.epoll.Epoll;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractNetServer<C extends CompConfig> extends AbstractNetCompImpl<C> {

    private final EventLoopGroup workerGroup = NetworkUtils.createEventLoopGroup(0);

    public AbstractNetServer(C config) {
        super(1, config);
    }


    @Override
    public CompletableFuture<Void> boot() {
        var future = new CompletableFuture<Void>();

        var bootstrap = new ServerBootstrap()
                .group(bossGroup(), workerGroup)
                .childHandler(new NetChannelInitializer(handler()))
                .channelFactory(NetworkUtils.generateChannelFactory())
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.IP_TOS, 24)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        if (config().tryTcpFastOpen() && Epoll.isTcpFastOpenServerSideAvailable()) {
            bootstrap.childOption(ChannelOption.TCP_FASTOPEN_CONNECT, true);
        }

        bootstrap.bind(config().hostname(), config().port()).addListener(it -> {
            if(it.isSuccess()) {
                this.onBindSuccess();
                future.complete(null);
                return;
            }
            this.onBindFail(it.cause());
            future.completeExceptionally(it.cause());
        });

        return future;
    }

    @Override
    public @NotNull CompletableFuture<Void> close() {
        var future = new CompletableFuture<Void>();
        this.bossGroup().shutdownGracefully().addListener(it -> onClose().whenComplete((unused, throwable) -> future.complete(null)));
        return future;
    }

    public abstract CompletableFuture<Void> onClose();

    public abstract void onBindFail(Throwable throwable);

    public abstract void onBindSuccess();

    public abstract NetCompHandler handler();
}
