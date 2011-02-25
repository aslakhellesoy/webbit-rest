package org.webbitserver.rest.chat;

import org.webbitserver.WebServer;
import org.webbitserver.handler.StaticFileHandler;
import org.webbitserver.handler.logging.LoggingHandler;
import org.webbitserver.handler.logging.SimpleLogSink;
import org.webbitserver.rest.resteasy.ResteasyHandler;

import static org.webbitserver.WebServers.createWebServer;

public class Main {

    public static void main(String[] args) throws Exception {
        MessagePublisher messagePublisher = new MessagePublisher();
        MessageResource messagesResource = new MessageResource(messagePublisher);
        SessionResource sessionResource = new SessionResource(messagePublisher);
        WebServer webServer = createWebServer(9876)
                .add(new LoggingHandler(new SimpleLogSink()))
//                .add(new Chatroom())
                .add(new StaticFileHandler("./src/test/java/org/webbitserver/rest/chat/content"))
                .add("/message-publisher", messagePublisher)
                .add(new ResteasyHandler(sessionResource, messagesResource))
                .start();

        System.out.println("Chat room running on: " + webServer.getUri());
    }

}
