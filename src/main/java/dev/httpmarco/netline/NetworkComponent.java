package dev.httpmarco.netline;

import dev.httpmarco.netline.channel.NetChannel;
import dev.httpmarco.netline.config.NetworkConfig;
import dev.httpmarco.netline.packet.Packet;
import dev.httpmarco.netline.tracking.Tracking;
import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface NetworkComponent<C extends NetworkConfig> {

    CompletableFuture<Void> boot();

    void stop();

    void responderOf(String id, Function<Void, Packet> responder);

    <T extends Tracking> NetworkComponent<C> track(Class<T> tracking, BiConsumer<NetChannel, T> apply);

    NetworkComponent<C> config(Consumer<C> config);

    NetworkComponentState state();

    @SneakyThrows
    default void bootNow() {
        this.boot().get(5, TimeUnit.SECONDS);
    }

}
