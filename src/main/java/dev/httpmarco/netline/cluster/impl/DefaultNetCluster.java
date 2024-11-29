package dev.httpmarco.netline.cluster.impl;

import dev.httpmarco.netline.cluster.NetCluster;
import dev.httpmarco.netline.cluster.node.LocalNetNode;
import dev.httpmarco.netline.cluster.node.NetNode;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DefaultNetCluster implements NetCluster {

    @Override
    public void searchHeadNode() {

    }

    @Override
    public @NonNull NetNode headNode() {
        return null;
    }

    @Override
    public @NonNull LocalNetNode localNode() {
        return null;
    }

    @Override
    public @NonNull List<NetNode> nodes() {
        return List.of();
    }

    @Override
    public @NonNull List<NetNode> availableNodes() {
        return List.of();
    }

    @Override
    public @Nullable NetNode findNode(String nodeId) {
        return null;
    }

    @Override
    public CompletableFuture<Void> close() {
        return null;
    }

    @Override
    public void closeSync() {

    }
}
