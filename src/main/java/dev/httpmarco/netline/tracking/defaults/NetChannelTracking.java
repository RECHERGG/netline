package dev.httpmarco.netline.tracking.defaults;

import dev.httpmarco.netline.channel.NetChannel;
import dev.httpmarco.netline.tracking.Tracking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class NetChannelTracking implements Tracking {

    private final NetChannel channel;

}
