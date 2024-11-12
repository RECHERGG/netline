package dev.httpmarco.netline.packet.base;

import dev.httpmarco.netline.packet.Packet;
import dev.httpmarco.netline.packet.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class StringBasePacket extends Packet {

    private String value;

    @Override
    public void read(PacketBuffer buffer) {
        this.value = buffer.readString();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeString(this.value);
    }
}
