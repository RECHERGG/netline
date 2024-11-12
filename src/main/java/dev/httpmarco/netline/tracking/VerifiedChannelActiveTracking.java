package dev.httpmarco.netline.tracking;

import dev.httpmarco.netline.channel.NetChannel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifiedChannelActiveTracking implements Tracking {

    private NetChannel channel;

}
