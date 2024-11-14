package dev.httpmarco.netline.request;

import dev.httpmarco.netline.packet.base.UuidBasePacket;
import java.util.UUID;

public final class BadRequestPacket extends UuidBasePacket {

    public BadRequestPacket(UUID uuid) {
        super(uuid);
    }
}
