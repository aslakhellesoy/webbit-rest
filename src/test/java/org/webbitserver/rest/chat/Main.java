package org.webbitserver.rest.chat;

import org.webbitserver.WebServer;
import org.webbitserver.handler.EmbeddedResourceHandler;
import org.webbitserver.handler.logging.LoggingHandler;
import org.webbitserver.handler.logging.SimpleLogSink;
import org.webbitserver.rest.resteasy.ResteasyHandler;

import static org.webbitserver.WebServers.createWebServer;

public class Main {

    public static void main(String[] args) throws Exception {
        Chatroom chatroom = new Chatroom();
        WebServer webServer = createWebServer(9876)
                .add(new LoggingHandler(new SimpleLogSink()))
                .add(new EmbeddedResourceHandler("org/webbitserver/rest/chat/content"))
                .add("/message-publisher", chatroom)
                .add(new ResteasyHandler(chatroom.resources()))
                .start();

        System.out.println("Chat room running on: " + webServer.getUri());
    }

}
