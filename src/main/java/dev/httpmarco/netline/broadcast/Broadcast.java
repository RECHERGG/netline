package dev.httpmarco.netline.broadcast;

import dev.httpmarco.netline.NetChannel;
import dev.httpmarco.netline.packet.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class Broadcast {

    private NetChannel[] channels;

    public Broadcast to(String... clientIds) {

        return this;
    }

    public Broadcast exclude(String... clientIds) {

        return this;
    }

    public Broadcast limit(int clientAmount){

        return this;
    }

    public void send(Packet packet) {
        for (var channel : this.channels) {
            channel.send(packet);
        }
    }

    public enum Receiver {
        SERVERS,
        CLIENTS
    }
}
