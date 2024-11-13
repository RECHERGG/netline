package dev.httpmarco.netline;

import dev.httpmarco.netline.client.NetClient;
import dev.httpmarco.netline.packet.TestSimplePacket;
import dev.httpmarco.netline.server.NetServer;
import dev.httpmarco.netline.tracking.ShutdownTracking;
import dev.httpmarco.netline.tracking.SuccessStartTracking;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;

import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
@Nested
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("NetLine - Communication component test")
public class NetlineTest {

    private static NetServer server;
    private static NetClient client;

    @BeforeAll
    public static void initialize() throws InterruptedException {
        server = Net.line().server();
        server.boot();

        server.track(ShutdownTracking.class, it -> log.info("Server shutdown tracking: {}", it));
        server.track(SuccessStartTracking.class, it -> log.info("Server start tracking: {}", it));

        Thread.sleep(1000);

        client = Net.line().client();
        client.boot();

        client.track(ShutdownTracking.class, it -> log.info("Client shutdown tracking: {}", it));
        client.track(SuccessStartTracking.class, it -> log.info("Client start tracking: {}", it));
    }

    @Test
    @Order(80)
    @DisplayName("[server] Check connection state is established")
    public void testServerState() {
        assert server.state() == NetworkComponentState.CONNECTION_ESTABLISHED;
    }

    @Test
    @Order(90)
    @DisplayName("[client] Check connection state is established")
    public void testClientState() throws InterruptedException {

        Thread.sleep(1000);

        assert client.state() == NetworkComponentState.CONNECTION_ESTABLISHED;
        assert server.channels().size() == 1;
    }

    @Test
    @Order(91)
    @DisplayName("[client] Send a simple test packet to server")
    public void clientSendPacket() throws InterruptedException {
        var result = new AtomicBoolean(false);

        server.track(TestSimplePacket.class, packet -> result.set(packet.completed()));
        client.send(new TestSimplePacket(true));

        Thread.sleep(500);
        assert result.get();
    }

    @Test
    @Order(91)
    @DisplayName("[server] Send every connect client a test packet")
    public void serverBroadcast() throws InterruptedException {
        var result = new AtomicBoolean(false);

        client.track(TestSimplePacket.class, packet -> result.set(packet.completed()));
        server.broadcast(new TestSimplePacket(true));

        Thread.sleep(1000);
        assert result.get();
    }

    @Test
    @Order(99)
    @DisplayName("[client] Close the connection")
    public void testClientClose() throws InterruptedException {
        client.stop();

        // we must wait for the close process here to be completed
        Thread.sleep(2500);

        assert client.state() == NetworkComponentState.CONNECTION_CLOSED;
        assert client.bossGroup().isShutdown();
        assert server.channels().isEmpty();
    }

    @Test
    @Order(100)
    @DisplayName("[server] Close the server binding")
    public void testStop() throws InterruptedException {
        server.stop();

        // we must wait for the close process here to be completed
        Thread.sleep(2500);

        assert server.state() == NetworkComponentState.CONNECTION_CLOSED;
        assert server.bossGroup().isShutdown();
    }
}
