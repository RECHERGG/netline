package dev.httpmarco.netline;

import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface Closeable {

    CompletableFuture<Void> close();

    @SneakyThrows
    default void closeSync() {
        this.close().get(5, TimeUnit.SECONDS);
    }
}
