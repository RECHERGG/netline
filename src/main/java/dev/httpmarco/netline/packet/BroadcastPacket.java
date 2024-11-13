package dev.httpmarco.netline.packet;

import dev.httpmarco.netline.packet.base.StringBasePacket;

public final class BroadcastPacket extends StringBasePacket {

    public BroadcastPacket(Packet packet) {
        super(packet.getClass().getName());
    }

    @Override
    public void read(PacketBuffer buffer) {
        super.read(buffer);

        // todo read packet
    }

    @Override
    public void write(PacketBuffer buffer) {
        super.write(buffer);

        // todo write packet
    }
}
