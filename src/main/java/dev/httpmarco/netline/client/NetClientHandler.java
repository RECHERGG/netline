package dev.httpmarco.netline.client;

import dev.httpmarco.netline.NetChannel;
import dev.httpmarco.netline.NetCompHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class NetClientHandler extends NetCompHandler {

    private static final Logger log = LogManager.getLogger(NetClientHandler.class);
    private final NetClient netClient;

    public NetClientHandler(NetClient netClient) {
        super(netClient);

        this.netClient = netClient;
    }

    @Override
    public void closeChannel(NetChannel netChannel) {

        if(netClient.bindProcess() != null) {
            log.error("Deny the bind process, because the channel close before the bind process complete -> whitelist");
            // deny the bind process, because the channel close before the bind process complete -> whitelist
            netClient.bindProcess().complete(null);
        }

        this.netClient.channel(null);
        this.netClient.state(NetClientState.CLOSED);
    }

    @Override
    public void handshakeChannel(NetChannel netChannel) {

    }
}
