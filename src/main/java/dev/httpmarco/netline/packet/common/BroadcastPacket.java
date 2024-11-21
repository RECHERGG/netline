package dev.httpmarco.netline.packet.common;

import dev.httpmarco.netline.packet.Packet;
import dev.httpmarco.netline.packet.basic.PacketBasePacket;

public final class BroadcastPacket extends PacketBasePacket {

    public BroadcastPacket(Packet packet) {
        super(packet);
    }
}
