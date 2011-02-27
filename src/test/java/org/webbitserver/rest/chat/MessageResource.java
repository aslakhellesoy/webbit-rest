package org.webbitserver.rest.chat;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;

@Path("/messages")
public class MessageResource {
    private final MessagePublisher messagePublisher;

    public MessageResource(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    // TODO: Why isn't @CookieParam working?
    @POST
    public void createMessage(@Context HttpHeaders hh, String message) {
        Cookie cookie = hh.getCookies().get("username");
        if (cookie != null) {
            messagePublisher.say(cookie.getValue(), message);
        }
    }

}
