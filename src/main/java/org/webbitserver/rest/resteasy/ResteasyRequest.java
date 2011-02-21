package org.webbitserver.rest.resteasy;

import org.jboss.resteasy.specimpl.PathSegmentImpl;
import org.jboss.resteasy.specimpl.UriInfoImpl;
import org.jboss.resteasy.spi.AsynchronousResponse;
import org.jboss.resteasy.util.HttpHeaderNames;
import org.jboss.resteasy.util.HttpRequestImpl;
import org.webbitserver.HttpRequest;

import javax.ws.rs.core.*;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    public static ResteasyRequest wrap(final HttpRequest request) {
        HttpHeaders headers = new HttpHeaders() {
            @Override
            public List<String> getRequestHeader(String name) {
                throw new UnsupportedOperationException();
            }

            @Override
            public MultivaluedMap<String, String> getRequestHeaders() {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<MediaType> getAcceptableMediaTypes() {
                String accepts = request.header(HttpHeaderNames.ACCEPT);
                MediaType mediaType = accepts == null ? MediaType.WILDCARD_TYPE : MediaType.valueOf(accepts);
                return Collections.singletonList(mediaType);
            }

            @Override
            public List<Locale> getAcceptableLanguages() {
                throw new UnsupportedOperationException();
            }

            @Override
            public MediaType getMediaType() {
                String contentType = request.header(HttpHeaderNames.CONTENT_TYPE);
                return contentType == null ? MediaType.WILDCARD_TYPE : MediaType.valueOf(contentType);
            }

            @Override
            public Locale getLanguage() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Map<String, Cookie> getCookies() {
                throw new UnsupportedOperationException();
            }
        };
        UriInfo uriInfo = new UriInfoImpl(null, null, request.uri(), null, PathSegmentImpl.parseSegments(request.uri()));
        return new ResteasyRequest(null, headers, request.method(), uriInfo);
    }
}
