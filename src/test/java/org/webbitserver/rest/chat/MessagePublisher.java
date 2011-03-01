package org.webbitserver.rest.chat;

import com.google.gson.Gson;
import org.webbitserver.CometConnection;
import org.webbitserver.CometHandler;

import java.util.ArrayList;
import java.util.List;

public class MessagePublisher implements CometHandler {
    public static final String USERNAME_KEY = "username";

    private final List<CometConnection> connections = new ArrayList<CometConnection>();
    private final Gson json = new Gson();

    static class Outgoing {
        enum Action {JOIN, LEAVE, SAY}

        Action action;
        String username;
        String message;
    }

    @Override
    public void onOpen(CometConnection connection) throws Exception {
        connections.add(connection);
    }

    @Override
    public void onClose(CometConnection connection) throws Exception {
        connections.remove(connection);
    }

    public void login(String username) {
        Outgoing outgoing = new Outgoing();
        outgoing.action = Outgoing.Action.JOIN;
        outgoing.username = username;
        broadcast(outgoing);
    }

    public void say(String username, String message) {
        Outgoing outgoing = new Outgoing();
        outgoing.action = Outgoing.Action.SAY;
        outgoing.username = username;
        outgoing.message = message;
        broadcast(outgoing);
    }

    private void broadcast(Outgoing outgoing) {
        String jsonStr = this.json.toJson(outgoing);
        for (CometConnection connection : connections) {
            connection.send(jsonStr);
        }
    }
}
