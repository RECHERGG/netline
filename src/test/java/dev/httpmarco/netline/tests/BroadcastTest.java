package dev.httpmarco.netline.tests;

import dev.httpmarco.netline.Net;
import dev.httpmarco.netline.client.NetClient;
import dev.httpmarco.netline.content.TestSimplePacket;
import dev.httpmarco.netline.server.NetServer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Nested
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("5 - Global broadcast test")
public final class BroadcastTest {

    private static NetClient netClientA;
    private static NetClient netClientB;

    private static NetServer netServer;

    @BeforeAll
    public static void initialize() {
        netServer = Net.line().server();
        netServer.bootSync();

        netClientA = Net.line().client();
        netClientA.bootSync();

        netClientB = Net.line().client();
        netClientB.bootSync();
    }

    @Test
    @Order(1)
    @DisplayName("5.1 Broadcast a simple packet to all clients")
    public void testSimpleBroadcast() {
        var result = new AtomicBoolean();

        netClientB.track(TestSimplePacket.class, packet -> result.set(true));
        netClientA.generateBroadcast().send(new TestSimplePacket(true));

        Awaitility.await().atMost(6, TimeUnit.SECONDS).untilTrue(result);
    }

    @Test
    @Order(2)
    @DisplayName("5.2 Broadcast a simple packet his self")
    public void testSelfBroadcastAlert() {
        var result = new AtomicBoolean();

        netClientA.track(TestSimplePacket.class, packet -> result.set(true));
        netClientA.generateBroadcast().enableBroadcastSelf().send(new TestSimplePacket(true));

        Awaitility.await().atMost(6, TimeUnit.SECONDS).untilTrue(result);
    }


    @AfterAll
    public static void shutdown() {
        netClientA.closeSync();
        netClientB.closeSync();
        netServer.closeSync();
    }
}
