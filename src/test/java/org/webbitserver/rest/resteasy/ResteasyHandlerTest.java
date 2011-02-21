package org.webbitserver.rest.resteasy;

import org.junit.Test;
import org.webbitserver.HttpHandler;
import org.webbitserver.stub.StubHttpControl;
import org.webbitserver.stub.StubHttpRequest;
import org.webbitserver.stub.StubHttpResponse;

import java.net.URL;

import static org.junit.Assert.*;

public class ResteasyHandlerTest {
    private HttpHandler handler;

    @Test
    public void responds_to_get() throws Exception {
        handler = handler();
        StubHttpResponse response = handle(request("/hello").method("GET"));
        assertEquals("Hello", response.contentsString());
        assertReturnedWithStatus(200, response);
    }

    @Test
    public void responds_with_method_not_allowed_for_post() throws Exception {
        handler = handler();
        StubHttpResponse response = handle(request("/hello").method("POST"));
        assertEquals("", response.contentsString());
        assertReturnedWithStatus(405, response);
    }

    @Test
    public void responds_with_404() throws Exception {
        handler = handler();
        StubHttpResponse response = handle(request("/nuffink").method("GET"));
        assertEquals("", response.contentsString());
        assertReturnedWithStatus(404, response);
    }

    private HttpHandler handler() {
        URL[] scanningUrls = new URL[]{getClass().getProtectionDomain().getCodeSource().getLocation()};
        return new ResteasyHandler(scanningUrls);
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
