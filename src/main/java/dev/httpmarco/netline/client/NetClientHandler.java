package dev.httpmarco.netline.client;

import dev.httpmarco.netline.NetChannel;
import dev.httpmarco.netline.NetCompHandler;

public final class NetClientHandler extends NetCompHandler {

    private final NetClient netClient;

    public NetClientHandler(NetClient netClient) {
        super(netClient);

        this.netClient = netClient;
    }

    @Override
    public void closeChannel(NetChannel netChannel) {
        this.netClient.channel(null);
        this.netClient.state(NetClientState.CLOSED);
    }

    @Override
    public void handshakeChannel(NetChannel netChannel) {

    }
}
