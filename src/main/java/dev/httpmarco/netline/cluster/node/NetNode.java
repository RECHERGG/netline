package dev.httpmarco.netline.cluster.node;

import lombok.NonNull;

import java.util.concurrent.CompletableFuture;

public interface NetNode {

    /**
     * Get the state of the node
     * @return the state
     */
    @NonNull NetNodeState state();

    /**
     * Change the state of the node
     * @param state the new state
     */
    void state(@NonNull NetNodeState state);

    /**
     * Check if the node is the head node
     * @return true if the node is the head node
     */
    boolean headNodeRole();

    /**
     * Boot the node
     * @return a future that will be completed when the node is booted
     */
    @NonNull CompletableFuture<Void> boot();

    /**
     * Boot the node synchronously
     */
    void bootSync();

    /**
     * Return the channel use state
     * @return true if channel, data and state are ready
     */
    boolean ready();

}
