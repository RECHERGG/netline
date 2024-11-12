package dev.httpmarco.netline.codec;

import dev.httpmarco.netline.packet.Packet;
import io.netty5.buffer.Buffer;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.handler.codec.MessageToByteEncoder;

public final class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected Buffer allocateBuffer(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
        return null;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, Buffer buffer) throws Exception {

    }
}
