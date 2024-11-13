package dev.httpmarco.netline.request;

import dev.httpmarco.netline.packet.Packet;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@UtilityClass
public class RequestRegister {

    private final Map<UUID, CompletableFuture<? extends Packet>> requests = new HashMap<>();

    public void register(UUID id, CompletableFuture<? extends Packet> future) {
        requests.put(id, future);
    }
}
