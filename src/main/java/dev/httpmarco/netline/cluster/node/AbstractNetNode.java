package dev.httpmarco.netline.cluster.node;

import dev.httpmarco.netline.cluster.NetCluster;
import io.netty5.channel.Channel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class AbstractNetNode implements NetNode {

    @NonNull
    private final NetCluster cluster;
    @NonNull
    private final NetNodeData data;
    @NotNull
    private NetNodeState state = NetNodeState.UNAVAILABLE;
    @Setter @Nullable
    private Channel channel;

    @Override
    public boolean headNodeRole() {
        return cluster.headNode().equals(this);
    }

    @Override
    public boolean ready() {
        return this.channel != null && this.channel.isActive() && this.state == NetNodeState.READY;
    }

    public void state(@NonNull NetNodeState state) {
        this.state = state;
    }
}
