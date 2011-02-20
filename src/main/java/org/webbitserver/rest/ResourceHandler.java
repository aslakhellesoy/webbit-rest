package org.webbitserver.rest;

import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

class ResourceHandler implements HttpHandler {
    private static final Map<String, Class> VERBS = new HashMap<String, Class>() {{
        put("GET", GET.class);
        put("PUT", PUT.class);
        put("POST", POST.class);
        put("DELETE", DELETE.class);
        put("HEAD", HEAD.class);
        put("OPTIONS", OPTIONS.class);
    }};

    private final Object resource;
    private final Executor executor;

    public ResourceHandler(Executor executor, Object resource) {
        this.resource = resource;
        this.executor = executor;
    }

    @Override
    public void handleHttpRequest(final HttpRequest request, final HttpResponse response, final HttpControl control) throws Exception {
        executor.execute(new ResourceWorker(request, control, response));
    }

    private class ResourceWorker implements Runnable {
        private final HttpRequest request;
        private final HttpControl control;
        private final HttpResponse response;

        public ResourceWorker(HttpRequest request, HttpControl control, HttpResponse response) {
            this.request = request;
            this.control = control;
            this.response = response;
        }

        @Override
        public void run() {
            control.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Method method = findMethod(VERBS.get(request.method().toUpperCase()));
                        String content = String.valueOf(method.invoke(resource));
                        response.content(content);
                    } catch (ResourceException e) {
                        response.status(e.status);
                    } catch (IllegalAccessException e) {
                        response.error(e);
                    } catch (InvocationTargetException e) {
                        response.error(e.getTargetException());
                    } finally {
                        response.end();
                    }
                }
            });
        }

        private Method findMethod(Class<Annotation> annotation) throws ResourceException {
            Method[] methods = resource.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(annotation)) {
                    return method;
                }
            }
            throw ResourceException.METHOD_NOT_ALLOWED;
        }
    }
}
