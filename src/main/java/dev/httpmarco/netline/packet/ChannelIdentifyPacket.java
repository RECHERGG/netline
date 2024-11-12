package dev.httpmarco.netline.packet;

import dev.httpmarco.netline.packet.base.StringBasePacket;

public final class ChannelIdentifyPacket extends StringBasePacket {

    public ChannelIdentifyPacket(String value) {
        super(value);
    }

    public String id() {
        return this.value();
    }
}
