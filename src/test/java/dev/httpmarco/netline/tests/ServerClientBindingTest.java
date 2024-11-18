package dev.httpmarco.netline.tests;

import dev.httpmarco.netline.AbstractNetLineTest;
import dev.httpmarco.netline.Net;
import dev.httpmarco.netline.NetworkComponentState;
import dev.httpmarco.netline.channel.NetChannelState;
import dev.httpmarco.netline.client.NetClient;
import dev.httpmarco.netline.server.NetServer;
import org.junit.jupiter.api.*;

@Nested
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("1 - Server-Client Binding Test")
public class ServerClientBindingTest extends AbstractNetLineTest {

    private static NetServer server;
    private static NetClient client;

    @Test
    @Order(1)
    @DisplayName("1.1 Bind server on default port 9090")
    public void bindServer() {
        server = Net.line().server();
        server.bootSync();

        assert server.state() == NetworkComponentState.CONNECTION_ESTABLISHED;
    }

    @Test
    @Order(2)
    @DisplayName("1.2 Bind client with the server")
    public void bindClient() {
        client = Net.line().client();
        client.bootSync();

        assert client.state() == NetworkComponentState.CONNECTION_ESTABLISHED;
        assert server.channels().size() == 1;
    }

    @Test
    @Order(3)
    @DisplayName("1.3 Shutdown client connection and check server channel state")
    public void closeClientConnection() {
        client.stopSync();

        assert client.state() == NetworkComponentState.CONNECTION_CLOSED;
        assert server.channels().isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("1.4 Try for a new reconnect")
    public void reconnectClient() {
        client.bootSync();

        assert client.state() == NetworkComponentState.CONNECTION_ESTABLISHED;
        // todo check order
        System.err.println("test channels: " + server.channels().size());
        assert server.channels().size() == 1;
    }

    @Test
    @Order(5)
    @DisplayName("1.5 Close server and check client state")
    public void serverShutdown() throws InterruptedException {
        server.stopSync();

        assert server.state() == NetworkComponentState.CONNECTION_CLOSED;
        assert server.channels().isEmpty();

        // we must wait for the client to close the connection
        Thread.sleep(200);

        assert client.state() == NetworkComponentState.CONNECTION_CLOSED;
    }
}