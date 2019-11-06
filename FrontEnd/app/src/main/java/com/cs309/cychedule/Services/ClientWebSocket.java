package com.cs309.cychedule.services;

import com.google.gson.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class ClientWebSocket extends WebSocketClient {

    public ClientWebSocket(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public ClientWebSocket(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        send("Connection Request");
        System.out.println("New connection has opened.");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection has closed because of: " + reason);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
    }

    @Override
    public void onError(Exception e) {
        System.err.println("Exception:" + e.toString());
    }

    public void sendJson(JsonObject jsonObject)
    {
        send(jsonObject.toString());
    }

    public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new ClientWebSocket(new URI("https://dev.hanzec.com/api/v1/auth/WebSocketDemo/"));
        client.connect();
    }
}
