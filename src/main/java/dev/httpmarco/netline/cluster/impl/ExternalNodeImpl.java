package dev.httpmarco.netline.cluster.impl;

import dev.httpmarco.netline.cluster.NetCluster;
import dev.httpmarco.netline.cluster.node.AbstractNetNode;
import dev.httpmarco.netline.cluster.node.NetNodeData;
import lombok.NonNull;

import java.util.concurrent.CompletableFuture;

public final class ExternalNodeImpl extends AbstractNetNode {

    public ExternalNodeImpl(@NonNull NetCluster cluster, @NonNull NetNodeData data) {
        super(cluster, data);
    }

    @Override
    public @NonNull CompletableFuture<Void> boot() {
        return null;
    }

    @Override
    public void bootSync() {

    }
}
