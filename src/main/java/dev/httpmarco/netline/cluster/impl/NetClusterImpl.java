package dev.httpmarco.netline.cluster.impl;

import dev.httpmarco.netline.cluster.NetCluster;
import dev.httpmarco.netline.cluster.node.LocalNetNode;
import dev.httpmarco.netline.cluster.node.NetNode;
import dev.httpmarco.netline.cluster.node.impl.LocalNodeImpl;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Getter
@Accessors(fluent = true)
public final class NetClusterImpl implements NetCluster {

    private final LocalNetNode localNode;
    private final List<NetNode> nodes = new LinkedList<>();

    public NetClusterImpl() {
        this.localNode = new LocalNodeImpl();
    }

    @Override
    public void searchHeadNode() {

    }

    @Override
    public @NonNull NetNode headNode() {
        return null;
    }

    @Override
    public @NonNull List<NetNode> availableNodes() {
        return this.nodes.stream().filter(NetNode::ready).toList();
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
    @SneakyThrows
    public void closeSync() {
        this.close().get(5, TimeUnit.SECONDS);
    }
}
