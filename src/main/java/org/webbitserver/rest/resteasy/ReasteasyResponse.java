package org.webbitserver.rest.resteasy;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.webbitserver.HttpResponse;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import java.io.IOException;
import java.io.OutputStream;

public class ReasteasyResponse implements org.jboss.resteasy.spi.HttpResponse {
    private final HttpResponse httpResponse;

    public ReasteasyResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
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
        return new MultivaluedMapImpl<String, Object>();
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
        throw new UnsupportedOperationException();
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
