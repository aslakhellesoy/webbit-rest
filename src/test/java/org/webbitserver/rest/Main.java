package org.webbitserver.rest;

import org.webbitserver.WebServer;
import org.webbitserver.rest.resteasy.ResteasyHandler;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static org.webbitserver.WebServers.createWebServer;

public class Main {

    @Path("/hello")
    public static class HelloResource {
        @GET
        @Produces("text/plain")
        public String getClichedMessage() {
            return "Hello";
        }
    }

    @Path("/world")
    public static class WorldResource {
        @GET
        @Produces("text/plain")
        public String getClichedMessage() {
            return "World";
        }
    }

    public static void main(String[] args) throws Exception {
        WebServer webServer = createWebServer(9877);
        webServer.add(new ResteasyHandler(Main.class));
        webServer.start();

        System.out.println("REST app running on: " + webServer.getUri());
    }

}