package org.webbitserver.rest.chat;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/sessions")
public class SessionResource {
    private final MessagePublisher messagePublisher;

    public SessionResource(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    @POST
    public void createSession(String username) {
        messagePublisher.login(username);
    }
}
