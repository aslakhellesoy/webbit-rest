package org.webbitserver.rest.resteasy;

import org.jboss.resteasy.core.Dispatcher;
import org.webbitserver.HttpControl;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;

public class ReasteasyResponse implements org.jboss.resteasy.spi.HttpResponse {
    private final HttpRequest request;
    private final HttpResponse response;
    private final HttpControl control;
    private MultivaluedMap<String, Object> outputHeaders;
    private boolean wasHandled;

    public ReasteasyResponse(HttpRequest request, HttpResponse response, Dispatcher dispatcher, HttpControl control) {
        this.request = request;
        this.response = response;
        this.control = control;
        this.outputHeaders = new WebbitResponseHeaders(response, dispatcher.getProviderFactory());
    }

    @Override
    public int getStatus() {
        return response.status();
    }

    @Override
    public void setStatus(int status) {
        if (status == 404) {
            wasHandled = false;
            control.nextHandler(request, response);
        } else {
            wasHandled = true;
            response.status(status);
        }
    }

    @Override
    public MultivaluedMap<String, Object> getOutputHeaders() {
        return outputHeaders;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new OutputStream() {
            @Override
            public void write(int i) throws IOException {
                response.content(new byte[]{(byte) i});
            }
        };
    }

    @Override
    public void addNewCookie(NewCookie cookie) {
        HttpCookie httpCookie = new HttpCookie(cookie.getName(), cookie.getValue());
        httpCookie.setComment(cookie.getComment());
        httpCookie.setDomain(cookie.getDomain());
        httpCookie.setMaxAge(cookie.getMaxAge());
        httpCookie.setVersion(cookie.getVersion());
        httpCookie.setPath(cookie.getPath());
        response.cookie(httpCookie);
    }

    @Override
    public void sendError(int status) throws IOException {
        response.status(status);
    }

    @Override
    public void sendError(int status, String message) throws IOException {
        setStatus(status);
    }

    @Override
    public boolean isCommitted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
    }

    public boolean wasHandled() {
        return wasHandled;
    }
}
