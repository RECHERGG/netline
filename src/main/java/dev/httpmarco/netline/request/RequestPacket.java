package dev.httpmarco.netline.request;

import dev.httpmarco.netline.packet.Packet;
import dev.httpmarco.netline.packet.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class RequestPacket extends Packet {

    private UUID requestId;
    private String id;

    @Override
    public void read(PacketBuffer buffer) {
        this.requestId = buffer.readUniqueId();
        this.id = buffer.readString();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUniqueId(this.requestId);
        buffer.writeString(this.id);
    }
}
