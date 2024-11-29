package dev.httpmarco.netline.cluster;

import dev.httpmarco.netline.Closeable;
import dev.httpmarco.netline.cluster.node.LocalNetNode;
import dev.httpmarco.netline.cluster.node.NetNode;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface NetCluster extends Closeable {

    /**
     * Search for the right new head node
     * These are a sync process
     */
    void searchHeadNode();

    /**
     * Get the current head node
     * @return the head node
     */
    @NonNull NetNode headNode();

    /**
     * Get the self runtime local node
     * @return the local node
     */
    @NonNull LocalNetNode localNode();

    /**
     * Get all the nodes in the cluster, ignore the state
     * @return a list of nodes
     */
    @NonNull List<NetNode> nodes();

    /**
     * Get all the available nodes in the cluster (state = RUNNING)
     * @return a list of available nodes
     */
    @NonNull List<NetNode> availableNodes();

    /**
     * Find a node by its id
     * @param nodeId the id of the node
     * @return the selected Node
     */
    @Nullable NetNode findNode(String nodeId);

}
