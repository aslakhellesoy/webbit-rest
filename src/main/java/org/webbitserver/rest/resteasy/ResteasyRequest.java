package org.webbitserver.rest.resteasy;

import org.jboss.resteasy.specimpl.PathSegmentImpl;
import org.jboss.resteasy.specimpl.UriBuilderImpl;
import org.jboss.resteasy.specimpl.UriInfoImpl;
import org.jboss.resteasy.spi.AsynchronousResponse;
import org.jboss.resteasy.util.HttpRequestImpl;
import org.webbitserver.HttpRequest;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;

public class ResteasyRequest extends HttpRequestImpl {
    public ResteasyRequest(InputStream inputStream, HttpHeaders httpHeaders, String httpMethod, UriInfo uri) {
        super(inputStream, httpHeaders, httpMethod, uri);
    }

    @Override
    public void setInputStream(InputStream stream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getAttribute(String attribute) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttribute(String name, Object value) {
    }

    @Override
    public void removeAttribute(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AsynchronousResponse createAsynchronousResponse(long suspendTimeout) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AsynchronousResponse getAsynchronousResponse() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void initialRequestThreadFinished() {
    }

    public static ResteasyRequest wrap(final HttpRequest request) throws UnsupportedEncodingException {
        HttpHeaders headers = new RestEasyHeaders(request);

        // see org.jboss.resteasy.plugins.server.servlet.ServletUtil
        URI uri = new UriBuilderImpl().path(request.uri()).build();

        UriInfo uriInfo = new UriInfoImpl(uri, uri, request.uri(), null, PathSegmentImpl.parseSegments(request.uri()));
        String body = request.body();
        InputStream in = body == null ? new ByteArrayInputStream(new byte[0]) : new ByteArrayInputStream(body.getBytes("UTF-8"));
        return new ResteasyRequest(in, headers, request.method(), uriInfo);
    }

}
