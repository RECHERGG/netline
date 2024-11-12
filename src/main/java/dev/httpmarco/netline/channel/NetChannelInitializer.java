package dev.httpmarco.netline.channel;

import dev.httpmarco.netline.NetworkComponentHandler;
import io.netty5.channel.Channel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class NetChannelInitializer extends io.netty5.channel.ChannelInitializer<Channel> {

    private NetworkComponentHandler componentHandler;

    @Override
    protected void initChannel(Channel channel) {
        channel.pipeline().addLast(componentHandler);
    }
}
