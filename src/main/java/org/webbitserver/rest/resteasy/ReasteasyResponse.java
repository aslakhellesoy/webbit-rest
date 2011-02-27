package org.webbitserver.rest.resteasy;

import org.jboss.resteasy.core.Dispatcher;
import org.webbitserver.HttpResponse;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;

public class ReasteasyResponse implements org.jboss.resteasy.spi.HttpResponse {
    private final HttpResponse httpResponse;
    private MultivaluedMap<String, Object> outputHeaders;

    public ReasteasyResponse(HttpResponse httpResponse, Dispatcher dispatcher) {
        this.httpResponse = httpResponse;
        this.outputHeaders = new WebbitResponseHeaders(httpResponse, dispatcher.getProviderFactory());
    }

    @Override
    public int getStatus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatus(int status) {
        httpResponse.status(status);
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
                httpResponse.content(new byte[]{(byte) i});
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
        httpResponse.cookie(httpCookie);
    }

    @Override
    public void sendError(int status) throws IOException {
        httpResponse.status(status);
    }

    @Override
    public void sendError(int status, String message) throws IOException {
        httpResponse.status(status);
    }

    @Override
    public boolean isCommitted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
    }
}
