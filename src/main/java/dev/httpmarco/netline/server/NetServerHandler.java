package dev.httpmarco.netline.server;

import dev.httpmarco.netline.NetChannel;
import dev.httpmarco.netline.NetCompHandler;
import dev.httpmarco.netline.tracking.BlacklistTracking;
import dev.httpmarco.netline.tracking.WhitelistTracking;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
public final class NetServerHandler extends NetCompHandler {

    private final NetServer netServer;

    public NetServerHandler(NetServer netServer) {
        super(netServer);
        this.netServer = netServer;
    }

    @Override
    public void closeChannel(NetChannel netChannel) {
        this.netServer.clients().remove(netChannel);
    }

    @Override
    public void handshakeChannel(@NotNull NetChannel netChannel) {
        var hostname = netChannel.hostname();
        var config = netServer.config();

        if ((!config.whitelist().isEmpty() && !config.whitelist().contains(hostname))) {
            netChannel.close();

            netServer.callTracking(netChannel, new WhitelistTracking(netChannel));
            log.info("Channel {} is not in the whitelist!", hostname);
            return;
        }

        if(!config.blacklist().isEmpty() && config.blacklist().contains(hostname) && !config.whitelist().contains(hostname)) {
            netChannel.close();

            netServer.callTracking(netChannel, new BlacklistTracking(netChannel));
            log.info("Channel {} is in the blacklist!", hostname);
            return;
        }
    }
}
