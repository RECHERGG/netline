package dev.httpmarco.netline.cluster.node.impl;

import dev.httpmarco.netline.cluster.node.LocalNetNode;
import dev.httpmarco.netline.cluster.node.NetNodeState;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.util.concurrent.CompletableFuture;

@Getter
@Accessors(fluent = true)
public class LocalNodeImpl implements LocalNetNode {

    private NetNodeState state = NetNodeState.UNAVAILABLE;

    @Override
    public boolean headNodeRole() {
        return false;
    }

    @Override
    public @NonNull CompletableFuture<Void> boot() {
        return null;
    }

    @Override
    public void bootSync() {

    }

    @Override
    public boolean ready() {
        // todo
        return this.state == NetNodeState.READY;
    }

    @Override
    public void state(@NonNull NetNodeState state) {
        this.state = state;
    }
}

