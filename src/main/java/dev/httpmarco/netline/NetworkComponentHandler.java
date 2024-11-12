package dev.httpmarco.netline;

import dev.httpmarco.netline.channel.NetChannel;
import dev.httpmarco.netline.packet.Packet;
import io.netty5.channel.Channel;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import org.jetbrains.annotations.NotNull;

public abstract class NetworkComponentHandler extends SimpleChannelInboundHandler<Packet> {

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Packet packet) {
        System.err.println("Received packet: " + packet);
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
