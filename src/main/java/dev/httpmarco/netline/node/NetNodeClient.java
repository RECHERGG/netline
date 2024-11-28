package dev.httpmarco.netline.node;

import dev.httpmarco.netline.client.NetClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class NetNodeClient extends NetClient {

    public NetNodeClient(@NotNull NetNodeBinding binding) {
        this.config().id(binding.id()).hostname(binding.hostname()).port(binding.port());
    }
}
