package org.webbitserver.rest.resteasy;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.plugins.server.servlet.ConfigurationBootstrap;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import static org.webbitserver.rest.resteasy.ResteasyRequest.wrap;

public class ResteasyHandler implements HttpHandler {
    private Dispatcher dispatcher;

    /**
     * Creates a new instance that finds resources by reflection.
     *
     * @param scanningUrls Classpath where Resteasy will look for JAX-RS annotated resources.
     */
    public ResteasyHandler(URL[] scanningUrls) {
        init(scanningUrls);
    }

    /**
     * Creates a new instance that finds resources by reflection.
     *
     * @param clazz Classpath where Resteasy will look for JAX-RS annotated resources.
     */
    public ResteasyHandler(Class clazz) {
        this(new URL[]{clazz.getProtectionDomain().getCodeSource().getLocation()});
    }

    /**
     * Creates a new instance that finds resources by reflection.
     * All JAX-RS resources available by the current ClassLoader will be registered.
     */
    public ResteasyHandler() {
        ClassLoader cl = getClass().getClassLoader();
        if (cl instanceof URLClassLoader) {
            init(((URLClassLoader) cl).getURLs());
        } else {
            throw new RuntimeException("The empty constructor can only be used when the class loader is an URLClassLoader. Use one of the other constructors.");
        }
    }

    /**
     * Creates a new instance with explicit resources. No reflection will be used.
     *
     * @param resources the resources to register.
     */
    public ResteasyHandler(Object... resources) {
        init(new URL[0], resources);
    }

    private void init(URL[] scanningUrls, Object... resources) {
        ConfigurationBootstrap bootstrap = new WebbitBootstrap(scanningUrls);
        ResteasyDeployment deployment = bootstrap.createDeployment();
        deployment.getResources().addAll(Arrays.asList(resources));
        deployment.start();
        dispatcher = deployment.getDispatcher();
    }

    @Override
    public void handleHttpRequest(final HttpRequest request, final HttpResponse response, final HttpControl control) throws Exception {
        ResteasyRequest req = wrap(request);
        ReasteasyResponse res = new ReasteasyResponse(request, response, dispatcher, control);
        dispatcher.invoke(req, res);
        if (res.wasHandled()) {
            response.end();
        }
    }
}
