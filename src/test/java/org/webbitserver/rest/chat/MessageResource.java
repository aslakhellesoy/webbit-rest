package org.webbitserver.rest.chat;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/messages")
public class MessageResource {
    private final MessagePublisher messagePublisher;

    public MessageResource(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    @POST
    public void createMessage(String message) {
        System.out.println("MESSAGE YES:"+ message);
        messagePublisher.say(message);
    }
}
