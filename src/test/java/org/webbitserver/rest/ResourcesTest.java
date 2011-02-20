package org.webbitserver.rest;

import org.junit.Test;
import org.webbitserver.HttpHandler;
import org.webbitserver.stub.StubHttpControl;
import org.webbitserver.stub.StubHttpRequest;
import org.webbitserver.stub.StubHttpResponse;

import javax.ws.rs.GET;
import java.util.concurrent.Executor;

import static org.junit.Assert.*;

public class ResourcesTest {
    private HttpHandler handler;
    
    private class Resource {
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
