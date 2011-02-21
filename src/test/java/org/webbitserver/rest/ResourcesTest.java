package org.webbitserver.rest;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.plugins.server.servlet.ConfigurationBootstrap;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.junit.Test;
import org.webbitserver.HttpHandler;
import org.webbitserver.rest.resteasy.WebbitBootstrap;
import org.webbitserver.rest.resteasy.WebbitHttpRequestImpl;
import org.webbitserver.rest.resteasy.WebbitHttpResponseImpl;
import org.webbitserver.stub.StubHttpControl;
import org.webbitserver.stub.StubHttpRequest;
import org.webbitserver.stub.StubHttpResponse;

import javax.ws.rs.GET;
import java.net.URL;
import java.util.concurrent.Executor;

import static org.junit.Assert.*;
import static org.webbitserver.rest.resteasy.WebbitHttpRequestImpl.*;

public class ResourcesTest {
    private HttpHandler handler;

    private class Resource {
        public Resource() {
            System.out.println("HERE I AM");
        }
        
        @GET
        public String get() {
            return "GET";
        }
    }

    @Test
    public void responds_to_get() throws Exception {
        handler = handler(new Resource());
        StubHttpResponse response = handle(request("/hello").method("GET"));
        assertReturnedWithStatus(200, response);
    }

    @Test
    public void responds_with_illegal_method() throws Exception {
        handler = handler(new Resource());
        StubHttpResponse response = handle(request("/hello").method("POST"));
        assertReturnedWithStatus(405, response);
    }

    @Test
    public void should_bootstrap() {
        URL[] scanningUrls = new URL[]{getClass().getProtectionDomain().getCodeSource().getLocation()};

        ConfigurationBootstrap bootstrap = new WebbitBootstrap(scanningUrls);
        ResteasyDeployment deployment = bootstrap.createDeployment();
        deployment.start();
        Dispatcher dispatcher = deployment.getDispatcher();
        WebbitHttpRequestImpl req = wrap(request("/hello").method("GET"));
        StubHttpResponse httpResponse = new StubHttpResponse();
        HttpResponse res = new WebbitHttpResponseImpl(httpResponse);
        dispatcher.invoke(req, res);
        assertEquals("Hello", httpResponse.contentsString());
    }

    private HttpHandler handler(Object resource) {
        Executor immediateExecutor = new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };
        return new ResourceHandler(immediateExecutor, resource);
    }

    private StubHttpResponse handle(StubHttpRequest request) throws Exception {
        StubHttpResponse response = new StubHttpResponse();
        handler.handleHttpRequest(request, response, new StubHttpControl(request, response));
        return response;
    }

    private StubHttpRequest request(String uri) {
        return new StubHttpRequest(uri);
    }

    private void assertReturnedWithStatus(int expectedStatus, StubHttpResponse response) {
        assertEquals(expectedStatus, response.status());
        assertTrue(response.ended());
        assertNull(response.error());
    }
}
