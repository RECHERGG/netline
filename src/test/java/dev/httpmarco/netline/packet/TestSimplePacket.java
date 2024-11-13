package dev.httpmarco.netline.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class TestSimplePacket extends Packet {

    private boolean completed;

    @Override
    public void read(PacketBuffer buffer) {
        this.completed = buffer.readBoolean();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeBoolean(completed);
    }
}
