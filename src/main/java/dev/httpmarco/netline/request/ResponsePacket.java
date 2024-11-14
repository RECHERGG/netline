package dev.httpmarco.netline.request;

import dev.httpmarco.netline.codec.PacketAllocator;
import dev.httpmarco.netline.packet.Packet;
import dev.httpmarco.netline.packet.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class ResponsePacket extends Packet {

    private UUID requestId;
    private Packet packet;

    @SneakyThrows
    @Override
    public void read(@NotNull PacketBuffer buffer) {
        this.requestId = buffer.readUniqueId();

        var className = buffer.readString();
        var packet = (Packet) PacketAllocator.allocate(Class.forName(className));

        packet.read(buffer);
        this.packet = packet;
    }

    @Override
    public void write(@NotNull PacketBuffer buffer) {
        buffer.writeUniqueId(this.requestId);
        buffer.writeString(this.packet.getClass().getName());
        this.packet.write(buffer);
    }
}
