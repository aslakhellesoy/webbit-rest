package org.webbitserver.rest.chat;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

@Path("/sessions")
public class SessionResource {
    @POST
    public Response createSession(String username) {
        return Response.ok().cookie(new NewCookie("username", username)).build();
    }
}
