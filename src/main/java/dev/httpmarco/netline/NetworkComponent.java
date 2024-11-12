package dev.httpmarco.netline;

import dev.httpmarco.netline.channel.NetChannel;
import dev.httpmarco.netline.config.NetworkConfig;
import dev.httpmarco.netline.tracking.Tracking;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface NetworkComponent<C extends NetworkConfig> {

    void boot();

    void stop();

    <T extends Tracking> NetworkComponent<C> track(Class<T> tracking, BiConsumer<NetChannel, T> apply);

    NetworkComponent<C> config(Consumer<C> config);

    NetworkComponentState state();

}
