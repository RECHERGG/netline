package dev.httpmarco.netline.cluster.impl;

import dev.httpmarco.netline.cluster.NetCluster;
import dev.httpmarco.netline.cluster.node.NetNode;
import dev.httpmarco.netline.cluster.node.NetNodeData;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Getter
@Accessors(fluent = true)
public final class NetClusterImpl implements NetCluster {

    private final LocalNodeImpl localNode;
    private final List<NetNode> nodes = new LinkedList<>();
    private NetNode headNode;

    public NetClusterImpl() {
        // todo find a config
        this.localNode = new LocalNodeImpl(this, new NetNodeData("nodeA"));
        this.nodes.add(this.localNode);
    }

    @Override
    public void searchHeadNode() {
        this.headNode = this.nodes.stream().filter(NetNode::ready).min(Comparator.comparingLong(it -> it.data().creationMillis())).orElseThrow();
    }

    @Override
    public @NonNull List<NetNode> availableNodes() {
        return this.nodes.stream().filter(NetNode::ready).toList();
    }

    @Override
    public @Nullable NetNode findNode(String nodeId) {
        return this.nodes.stream().filter(it -> it.data().id().equals(nodeId)).findFirst().orElse(null);
    }

    @Override
    public void registerNode(NetNodeData data) {

    }

    @Override
    public void unregisterNode(NetNodeData data) {

    }

    @Contract(pure = true)
    @Override
    public @Nullable CompletableFuture<Void> close() {
        return null;
    }

    @Override
    @SneakyThrows
    public void closeSync() {
        this.close().get(5, TimeUnit.SECONDS);
    }
}
