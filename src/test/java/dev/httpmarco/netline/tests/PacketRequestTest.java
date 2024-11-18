package dev.httpmarco.netline.tests;

import dev.httpmarco.netline.Net;
import dev.httpmarco.netline.client.NetClient;
import dev.httpmarco.netline.packet.TestSimplePacket;
import dev.httpmarco.netline.request.ResponderRegisterPacket;
import dev.httpmarco.netline.server.NetServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("4 - Packet request test")
public final  class PacketRequestTest {

    private static final Logger log = LogManager.getLogger(PacketRequestTest.class);
    private static NetServer server;
    private static NetClient client;

    @BeforeAll
    public static void createTestSetup() {
        server = Net.line().server();
        client = Net.line().client();

        server.bootSync();
        client.bootSync();
    }


    @Test
    @Order(1)
    @DisplayName("4.1 External responder test")
    public void clientToServerRequests() {
        var responderId = "testA";
        var result = new AtomicBoolean(false);

        server.track(ResponderRegisterPacket.class, (_, responderRegisterPacket) -> {
            System.out.println(responderRegisterPacket.id());
            if(responderRegisterPacket.id().equals(responderId)) {
                result.set(true);
            }
        });

        client.responderOf(responderId, _ -> new TestSimplePacket(true));
        Awaitility.await().atMost(6, TimeUnit.SECONDS).untilTrue(result);
        assert server.responders().size() == 1;
        assert server.responders().containsKey(responderId);
    }

    @Test
    @Order(2)
    @DisplayName("4.2 Send a requests to a specific client")
    public void responderServerToClientRequestTest() throws InterruptedException {
        var responderId = "testA";
        var asyncResult = new AtomicBoolean(false);

        // first test async request
        server.channels().get(0).requestAsync(responderId, TestSimplePacket.class).whenComplete((testSimplePacket, throwable) -> asyncResult.set(true));

        Awaitility.await().atMost(6, TimeUnit.SECONDS).untilTrue(asyncResult);

        var request = server.channels().get(0).request(responderId, TestSimplePacket.class);
        assert request != null;
        assert request.completed();
        assert asyncResult.get();
    }

    @Test
    @Order(3)
    @DisplayName("4.3 Send a requests to a specific client")
    public void responderClientToServerRequestTest() {
        var responderId = "testA";
        var result = new AtomicBoolean(false);

        // first test async request
        client.channel().requestAsync(responderId, TestSimplePacket.class).whenComplete((testSimplePacket, throwable) -> result.set(true));

        Awaitility.await().atMost(6, TimeUnit.SECONDS).untilTrue(result);
        var request = client.channel().request(responderId, TestSimplePacket.class);
        assert request != null;
        assert request.completed();

        assert result.get();
    }


    @AfterAll
    public static void closeAll() {
        client.stopSync();
        server.stopSync();
    }
}
