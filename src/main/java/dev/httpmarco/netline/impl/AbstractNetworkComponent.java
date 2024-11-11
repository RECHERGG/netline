package dev.httpmarco.netline.impl;

import dev.httpmarco.netline.NetworkComponent;
import dev.httpmarco.netline.NetworkComponentState;
import dev.httpmarco.netline.config.NetworkConfig;
import dev.httpmarco.netline.tracking.ShutdownTracking;
import dev.httpmarco.netline.tracking.SuccessStartTracking;
import dev.httpmarco.netline.tracking.Tracking;
import dev.httpmarco.netline.utils.NetworkUtils;
import io.netty5.channel.Channel;
import io.netty5.channel.EventLoopGroup;
import io.netty5.util.concurrent.FutureListener;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Getter
@Accessors(fluent = true)
public abstract class AbstractNetworkComponent<C extends NetworkConfig> implements NetworkComponent<C> {

    private final Map<Class<? extends Tracking>, List<Consumer<? extends Tracking>>> trackers = new HashMap<>();
    private final EventLoopGroup bossGroup;
    private final C config;

    @Setter(AccessLevel.PROTECTED)
    private NetworkComponentState state = NetworkComponentState.INITIALIZING;

    public AbstractNetworkComponent(int bossGroupThreads, C config) {
        this.bossGroup = NetworkUtils.createEventLoopGroup(bossGroupThreads);
        this.config = config;
    }

    @Override
    public void stop() {
        this.bossGroup.shutdownGracefully().addListener(it -> {
            this.state = NetworkComponentState.CONNECTION_CLOSED;
            callTracking(new ShutdownTracking());
        });
    }

    @Override
    public NetworkComponent<C> config(@NotNull Consumer<C> config) {
        config.accept(this.config);
        return this;
    }

    @Override
    public <T extends Tracking> NetworkComponent<C> track(Class<T> tracking, Consumer<T> apply) {
        var currentTrackers = trackers.getOrDefault(tracking, new ArrayList<>());
        currentTrackers.add(apply);
        this.trackers.put(tracking, currentTrackers);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Tracking> void callTracking(@NotNull T tracking) {
        if(trackers.containsKey(tracking.getClass())) {
            trackers.get(tracking.getClass()).forEach(it -> ((Consumer<Tracking>) it).accept(tracking));
        }
    }

    public FutureListener<? super Channel> handleConnectionRelease() {
        return it -> {
            if(it.isSuccess()) {
                state(NetworkComponentState.CONNECTION_ESTABLISHED);
                callTracking(new SuccessStartTracking());
            } else {
                state(NetworkComponentState.CONNECTION_FAILED);
                //todo: call tracking
            }
        };
    }
}
