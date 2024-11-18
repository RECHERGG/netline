package dev.httpmarco.netline.impl;

import dev.httpmarco.netline.NetworkComponent;
import dev.httpmarco.netline.NetworkComponentState;
import dev.httpmarco.netline.channel.NetChannel;
import dev.httpmarco.netline.config.NetworkConfig;
import dev.httpmarco.netline.packet.Packet;
import dev.httpmarco.netline.request.BadRequestPacket;
import dev.httpmarco.netline.request.RequestPacket;
import dev.httpmarco.netline.request.RequestRegister;
import dev.httpmarco.netline.request.ResponsePacket;
import dev.httpmarco.netline.tracking.ShutdownTracking;
import dev.httpmarco.netline.tracking.SuccessStartTracking;
import dev.httpmarco.netline.tracking.Tracking;
import dev.httpmarco.netline.utils.NetworkUtils;
import io.netty5.channel.Channel;
import io.netty5.channel.EventLoopGroup;
import io.netty5.util.concurrent.FutureListener;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@Getter
@Log4j2
@Accessors(fluent = true)
public abstract class AbstractNetworkComponent<C extends NetworkConfig> implements NetworkComponent<C> {

    private final Map<Class<? extends Tracking>, List<BiConsumer<NetChannel, ? extends Tracking>>> trackers = new HashMap<>();
    private final Map<String, Function<Void, Packet>> responders = new HashMap<>();
    private final int bossGroupThreads;
    private final C config;


    private EventLoopGroup bossGroup;

    @Setter
    private NetworkComponentState state = NetworkComponentState.INITIALIZING;

    public AbstractNetworkComponent(int bossGroupThreads, C config) {
        this.bossGroupThreads = bossGroupThreads;
        this.config = config;
        this.reinitializeEventLoopGroup();

        track(ResponsePacket.class, (channel, responsePacket) -> {
            if(!RequestRegister.contains(responsePacket.requestId())) {
                log.warn("Request {} not found!", responsePacket.requestId());
                return;
            }
            RequestRegister.apply(responsePacket.requestId(), responsePacket.packet());
        });

        track(RequestPacket.class, (channel, requestPacket) -> {
            if (!responders().containsKey(requestPacket.id())) {
                channel.send(new BadRequestPacket(requestPacket.requestId()));
                return;
            }
            channel.send(new ResponsePacket(requestPacket.requestId(), this.responders().get(requestPacket.id()).apply(null)));
        });
    }

    @Override
    public CompletableFuture<Void> stop() {
        var future = new CompletableFuture<Void>();
        this.bossGroup.shutdownGracefully().addListener(_ -> {
            this.state = NetworkComponentState.CONNECTION_CLOSED;

            future.complete(null);
            callTracking(null, new ShutdownTracking());
        });
        return future;
    }

    @Override
    public NetworkComponent<C> config(@NotNull Consumer<C> config) {
        config.accept(this.config);
        return this;
    }

    public <T extends Tracking> NetworkComponent<C> track(Class<T> tracking, Consumer<T> apply) {
        return this.track(tracking, (_, tracking1) -> apply.accept(tracking1));
    }

    @Override
    public <T extends Tracking> NetworkComponent<C> track(Class<T> tracking, BiConsumer<NetChannel, T> apply) {
        var currentTrackers = trackers.getOrDefault(tracking, new ArrayList<>());
        currentTrackers.add(apply);
        this.trackers.put(tracking, currentTrackers);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Tracking> void callTracking(NetChannel channel, @NotNull T tracking) {
        if (trackers.containsKey(tracking.getClass())) {
            trackers.get(tracking.getClass()).forEach(it -> ((BiConsumer<NetChannel, Tracking>) it).accept(channel, tracking));
        }
    }

    public FutureListener<? super Channel> handleConnectionRelease() {
        return it -> {
            if (it.isSuccess()) {
                state(NetworkComponentState.CONNECTION_ESTABLISHED);
                callTracking(null, new SuccessStartTracking());
            } else {
                state(NetworkComponentState.CONNECTION_FAILED);
                //todo: call tracking
            }
        };
    }

    @Override
    public void responderOf(String id, Function<Void, Packet> responder) {
        this.responders.put(id, responder);
    }

    public void reinitializeEventLoopGroup() {
        this.bossGroup = NetworkUtils.createEventLoopGroup(bossGroupThreads);
    }
}
