package dev.httpmarco.netline.cluster.node;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class NetNodeData {

    // the oldest node data represent the head node
    private final long creationMillis = System.currentTimeMillis();

}
