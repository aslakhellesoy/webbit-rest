package org.webbitserver.rest;

import org.webbitserver.WebServer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static org.webbitserver.WebServers.createWebServer;

public class Main {

    @Path("/helloworld")
    public static class HelloWorldResource {
        @GET
        @Produces("text/plain")
        public String getClichedMessage() {
            return "Hello World";
        }
    }

    public static void main(String[] args) throws Exception {
        WebServer webServer = createWebServer(9877);
        new Resources(new HelloWorldResource()).addTo(webServer);
        webServer.start();

        System.out.println("REST app running on: " + webServer.getUri());
    }

}