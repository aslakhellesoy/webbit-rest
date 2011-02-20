package org.webbitserver.rest;

import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

import javax.ws.rs.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ResourceHandler implements HttpHandler {
    private final Object resource;
    private static final Map<String,Class> VERBS = new HashMap<String,Class>() {{
        put("GET", GET.class);
        put("PUT", PUT.class);
        put("POST", POST.class);
        put("DELETE", DELETE.class);
        put("HEAD", HEAD.class);
        put("OPTIONS", OPTIONS.class);
    }};

    public ResourceHandler(Object resource) {
        this.resource = resource;
    }

    @Override
    public void handleHttpRequest(HttpRequest request, final HttpResponse response, HttpControl control) throws Exception {
        String verb = request.method().toUpperCase();
        final Method method = findMethod(verb);
        String content = String.valueOf(method.invoke(resource));
        response.content(content).end();
    }

    private Method findMethod(String verb) {
        Class ann = annotationForVerb(verb);
        Method[] methods = resource.getClass().getMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(ann)) {
                return method;
            }
        }
        throw new IllegalStateException("No methods tagged with " + ann);
    }

    private Class annotationForVerb(String verb) {
        return VERBS.get(verb);
    }
}
