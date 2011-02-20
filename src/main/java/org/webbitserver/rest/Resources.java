package org.webbitserver.rest;

import org.webbitserver.WebServer;

import javax.ws.rs.Path;
import java.util.concurrent.Executor;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class Resources {
    private final Executor executor;
    private final Object[] resources;

    public Resources(Executor executor, Object... resources) {
        this.executor = executor;
        this.resources = resources;
    }

    public Resources(Object... resources) {
        this(newFixedThreadPool(4), resources);
    }
    
    public void addTo(WebServer webServer) {
        for (Object resource : resources) {
            addResource(resource, webServer);
        }
    }

    private void addResource(Object resource, WebServer webServer) {
        Path path = resource.getClass().getAnnotation(Path.class);
        webServer.add(path.value(), new ResourceHandler(executor, resource));
    }
}
