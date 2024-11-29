package dev.httpmarco.netline.cluster.node;

public enum NetNodeState {

    // if the node is not available and cannot be used
    UNAVAILABLE,
    // node sync to same data as the other nodes
    SYNCING,
    // node is ready to be used
    READY,
    // node is closed and cannot be used
    CLOSED,

}
