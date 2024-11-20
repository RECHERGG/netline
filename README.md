> We have fixed this bug in our development branch.
> You can view the commit here: 

## 1. Generell

### 1.1 Create a new comp component
Below is an example of a simple server component. The API is similar for a client or, in the future, a node.
```java
Net.line()
    // Set the type of the component.
    // You can also use: client or, in the future, a node.
    .server()
    // If you want to configure your component, you can do the following:
    .config(it -> {
        // Set the hostname for binding. The default is '0.0.0.0'.
        it.hostname("xx.xx.xx.xx");
        // Set the port for binding. The default is '9091'.
        it.port(1234);
    })
    // Register a new channel tracking. See the tracking table for more details.
    .track(YourExamplePacket.class, (channel, packet) -> {
        // Add your logic here when 'YourExamplePacket' is received.
        System.out.println("Packet received! Hello!");
    });
```

### 1.2 Request and Response
With Netline, you can easily send a request packet to a component (comp) and receive a response. This can be used either asynchronously or synchronously.
```java
// For every request, you need a responder to handle the request and send back a response. 
// You can create it as follows:
// Important: The request ID and responder ID must match for each request-response pair!
server.responderOf("your_custom_request_id", (channel, id) -> new ResponsePacketType());

var request = client.request("your_custom_request_id", ResponsePacketType.class);

// Get the response of your request synchronously.
// The default timeout limit is 5 seconds. You can customize this in your component configuration.
ResponsePacketType response = request.sync();

// The same request but handled asynchronously.
request.async().whenComplete((result, throwable) -> {
    if (throwable != null) {
        // The request failed. You must check the cause of the exception!
        throwable.printStackTrace();
        return;
    }
    // The request was successfully answered.
    ResponsePacketType asyncResponse = result;
});

```

## 2. Custom comp 

### 2.1 Custom client comp methods
```java

```

### 2.2 Custom server comp methods
```java

```

## 3. Security

### 3.1 Blacklist and Whitelist
```java

```

### 3.2 Custom security adapter
The security provider allow you, to manage your clients with a custom security policy. Block or allow clients, based on your custom logic. A simpler way are the blacklist and whitelist. 
```java
// set your custom security provider -> child of @SecurityProvider
server.withSecurityPolicy(new YourCustomSecurityProvider());
```
Example of a simple security provider:
```java
public class YourCustomSecurityProvider implement SecurityProvider {

    @Override
    public void detectUnauthorizedAccess(NetChannel netChannel) {
        // alert if a client tries to connect without permission
        // Channel closed automatically after this method.
        System.err.println("Unauthorized access detected");
    }

    @Override
    public boolean authenticate(NetChannel netChannel) {
        // check the id of the client. Here you can implement your custom logic.
        return netChannel.id().equals("testA");
    }
}
```

- [ ] Implement custom timeout property
- [ ] Implement fast open on server and client -> configurable
- [ ] Tracking for client connect, client disconnect
- [ ] Node implementation