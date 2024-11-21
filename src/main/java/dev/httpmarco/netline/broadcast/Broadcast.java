package dev.httpmarco.netline.broadcast;

import dev.httpmarco.netline.NetChannel;
import dev.httpmarco.netline.impl.AbstractNetCompImpl;
import dev.httpmarco.netline.packet.Packet;
import dev.httpmarco.netline.packet.common.BroadcastPacket;
import lombok.AllArgsConstructor;

public final class Broadcast {

    private final AbstractNetCompImpl<?> comp;
    private final NetChannel selfChannel;
    private final NetChannel[] channels;
    private boolean broadcastSelf = false;

    public Broadcast(AbstractNetCompImpl<?> comp, NetChannel selfChannel, NetChannel[] channels) {
        this.comp = comp;
        this.selfChannel = selfChannel;
        this.channels = channels;
    }

    public Broadcast to(String... clientIds) {

        return this;
    }

    public Broadcast enableBroadcastSelf() {
        this.broadcastSelf = true;
        return this;
    }

    public Broadcast exclude(String... clientIds) {

        return this;
    }

    public Broadcast limit(int clientAmount){

        return this;
    }

    public void send(Packet packet) {

        if(this.broadcastSelf) {
            this.comp.callTracking(this.selfChannel, packet);
        }

        packet = new BroadcastPacket(packet);
        for (var channel : this.channels) {
            channel.send(packet);
        }
    }

    public enum Receiver {
        SERVERS,
        CLIENTS
    }
}
