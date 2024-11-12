<div align="center">
# Netline
</div>

### Configure the network platform
In this case, a simple server reference will be used, but the API is the same on the client and node side.
```java
Net.line()
    // Initializes the server on this connection (Possible types: client, node)
    .server()
    
    // Configures the communication component with specific settings
    .config(it -> {
        // Sets the server's port to 8080
        // Default value is: `9091`
        it.port(8080);
        // Sets the server's hostname to the local IP address
        // Default value is `0.0.0.0`
        it.hostname("127.0.0.1");
    })
    // Adds tracking for server shutdown events. More Tracking types are be listed below
    .track(ShutdownTracking.class, it -> log.info("Server shutdown tracking: {}", it))
        
    // Adds tracking for successful server start events
    .track(SuccessStartTracking.class, it -> log.info("Server start tracking: {}", it))
        
    // Binds and starts the server with the above configurations and tracking settings
    .bind();

```
### All tracking types
All tracking types are listed below. The tracking types can be used on the server, node, and client side.

| Tracking Type        | Components           | Description                                                                                           |
|----------------------|----------------------|-------------------------------------------------------------------------------------------------------|
| ShutdownTracking     | Server, Node, Client | Tracks the component shutdown event                                                                   |
| SuccessStartTracking | Server, Node, Client | Tracks the component start event. On Client: Only call if the connection is successfully established. |

### Coming features

- Hostname and port whitelist 
- Packet History
- Security adapter
- Class supplier configuration