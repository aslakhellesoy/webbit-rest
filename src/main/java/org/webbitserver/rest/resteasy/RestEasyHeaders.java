package org.webbitserver.rest.resteasy;

import org.jboss.resteasy.util.HttpHeaderNames;
import org.jboss.resteasy.util.MediaTypeHelper;
import org.webbitserver.HttpRequest;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

class RestEasyHeaders implements HttpHeaders {
    private final HttpRequest request;

    public RestEasyHeaders(HttpRequest request) {
        this.request = request;
    }

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
        String accept = request.header(HttpHeaderNames.ACCEPT);
        return accept == null ? Collections.<MediaType>emptyList() : MediaTypeHelper.parseHeader(accept);
    }

    @Override
    public List<Locale> getAcceptableLanguages() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MediaType getMediaType() {
        String contentType = request.header(HttpHeaderNames.CONTENT_TYPE);
        return contentType == null ? null : MediaType.valueOf(contentType);
    }

    @Override
    public Locale getLanguage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Cookie> getCookies() {
        throw new UnsupportedOperationException();
    }
}
