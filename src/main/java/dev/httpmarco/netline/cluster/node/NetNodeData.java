package dev.httpmarco.netline.cluster.node;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class NetNodeData {

    // the oldest node data represent the head node
    private final long creationMillis = System.currentTimeMillis();
    // unique node id
    private final String id;

}
