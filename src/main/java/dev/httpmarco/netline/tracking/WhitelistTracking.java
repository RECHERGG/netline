package dev.httpmarco.netline.tracking;

import dev.httpmarco.netline.channel.NetChannel;
import dev.httpmarco.netline.tracking.defaults.NetChannelTracking;

public final class WhitelistTracking extends NetChannelTracking {

    public WhitelistTracking(NetChannel channel) {
        super(channel);
    }
}
