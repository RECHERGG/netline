package dev.httpmarco.netline.node;

public enum NetNodeState {

    // if node is present, but not started booting
    INITIALIZING,
    // if node is booting and failed to bind to cluster
    FAILED,
    // if node is booting and binding to cluster (ip pool)
    BIND_CLUSTER,
    // if node is booting and syncing with cluster
    SYNC_CLUSTER,
    // if node is booting and ready to accept connections
    READY,
    // if node is shutting down
    CLOSED


}
