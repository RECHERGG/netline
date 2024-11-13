package dev.httpmarco.netline;

import dev.httpmarco.netline.channel.NetChannel;
import dev.httpmarco.netline.channel.NetChannelState;
import dev.httpmarco.netline.impl.AbstractNetworkComponent;
import dev.httpmarco.netline.packet.ChannelIdentifyPacket;
import dev.httpmarco.netline.packet.Packet;
import io.netty5.channel.Channel;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public abstract class NetworkComponentHandler extends SimpleChannelInboundHandler<Packet> {

    private static final Logger log = LogManager.getLogger(NetworkComponentHandler.class);
    private AbstractNetworkComponent<?> component;

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Packet packet) {
        var netChannel = findChannel(channelHandlerContext.channel());

        if(netChannel == null){
            channelHandlerContext.close();
            return;
        }

        if(netChannel.state() != NetChannelState.READY && !(packet instanceof ChannelIdentifyPacket)){
            log.warn("Channel {} is not ready to receive packets", netChannel);
            return;
        }

        component.callTracking(netChannel, packet);
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx)  {
        handshakeChannel(new NetChannel(ctx.channel()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        closeChannel(findChannel(ctx.channel()));
    }

    public abstract NetChannel findChannel(Channel channel);

    public abstract void handshakeChannel(NetChannel netChannel);

    public abstract void closeChannel(NetChannel netChannel);
}
