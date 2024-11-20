> We have fixed this bug in our development branch.
> You can view the commit here: 

## 1.1 Create a new comp component
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

## 2.2 Request and Response
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