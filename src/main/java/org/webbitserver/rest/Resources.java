package org.webbitserver.rest;

import org.webbitserver.WebServer;

import javax.ws.rs.Path;

public class Resources {
    private final Object[] resources;

    public Resources(Object... resources) {
        this.resources = resources;
    }

    public void addTo(WebServer webServer) {
        for (Object resource : resources) {
            addResource(resource, webServer);
        }
    }

    private void addResource(Object resource, WebServer webServer) {
        Path path = resource.getClass().getAnnotation(Path.class);
        webServer.add(path.value(), new ResourceHandler(resource));
    }
}
