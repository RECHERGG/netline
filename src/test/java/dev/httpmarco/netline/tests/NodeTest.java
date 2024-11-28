package dev.httpmarco.netline.tests;

import dev.httpmarco.netline.Net;
import dev.httpmarco.netline.node.NetNode;
import dev.httpmarco.netline.node.NetNodeBinding;
import dev.httpmarco.netline.node.NetNodeState;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("6 - Node cluster test")
public final class NodeTest {

    private static NetNode nodeA;
    private static NetNode nodeB;

    @BeforeAll
    public static void beforeHandling() {
        nodeA = Net.line().node();
        nodeA.config(it -> it.bindings().add(new NetNodeBinding("nodeA", "localhost", 9093)));
        nodeB = Net.line().node();
        nodeB.config(it -> it.port(9093));
    }

    @Test
    @Order(1)
    @DisplayName("6.1 Start first node")
    public void testState() {
        assert nodeA.state() == NetNodeState.INITIALIZING;
        nodeA.bootSync();
        assert nodeA.state() == NetNodeState.READY;
    }

    @Test
    @Order(2)
    @DisplayName("6.2 Start second node")
    public void testSecondNode() {
        assert nodeB.state() == NetNodeState.INITIALIZING;
        nodeB.bootSync();
        assert nodeB.state() == NetNodeState.READY;
    }

}
