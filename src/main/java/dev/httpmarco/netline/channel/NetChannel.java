package dev.httpmarco.netline.channel;

import io.netty5.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class NetChannel {

    private String id;
    private final Channel channel;

    @Setter
    private NetChannelState state = NetChannelState.AUTHENTICATION_PENDING;

    public NetChannel(Channel channel) {
        this.channel = channel;
    }
}
