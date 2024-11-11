package dev.httpmarco.netline;

import dev.httpmarco.netline.client.NetClient;
import dev.httpmarco.netline.server.NetServer;
import dev.httpmarco.netline.tracking.ShutdownTracking;
import dev.httpmarco.netline.tracking.SuccessStartTracking;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;

@Log4j2
@Nested
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("NetLine - Communication component test")
public class NetServerTest {

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
    @DisplayName("Server state check")
    public void testServerState() {
        assert server.state() == NetworkComponentState.CONNECTION_ESTABLISHED;
    }

    @Test
    @Order(90)
    @DisplayName("Client state check")
    public void testClientState() {
        assert client.state() == NetworkComponentState.CONNECTION_ESTABLISHED;
    }

    @Test
    @Order(99)
    @DisplayName("Client close test")
    public void testClientClose() throws InterruptedException {
        client.stop();

        // we must wait for the close process here to be completed
        Thread.sleep(2500);

        assert client.state() == NetworkComponentState.CONNECTION_CLOSED;
        assert client.bossGroup().isShutdown();
        assert server.clients().isEmpty();
    }

    @Test
    @Order(100)
    @DisplayName("Server close test")
    public void testStop() throws InterruptedException {
        server.stop();

        // we must wait for the close process here to be completed
        Thread.sleep(2500);

        assert server.state() == NetworkComponentState.CONNECTION_CLOSED;
        assert server.bossGroup().isShutdown();
    }
}
