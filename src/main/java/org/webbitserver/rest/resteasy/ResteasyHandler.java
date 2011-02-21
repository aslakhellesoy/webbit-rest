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

import static org.webbitserver.rest.resteasy.ResteasyRequest.wrap;

public class ResteasyHandler implements HttpHandler {
    private Dispatcher dispatcher;

    public ResteasyHandler(URL[] scanningUrls) {
        init(scanningUrls);
    }

    public ResteasyHandler(Class clazz) {
        this(new URL[]{clazz.getProtectionDomain().getCodeSource().getLocation()});
    }

    public ResteasyHandler() {
        ClassLoader cl = getClass().getClassLoader();
        if(cl instanceof URLClassLoader) {
            init(((URLClassLoader) cl).getURLs());
        } else {
            throw new RuntimeException("The empty constructor can only be used when the class loader is an URLClassLoader. Use one of the other constructors.");
        }
    }

    private void init(URL[] scanningUrls) {
        ConfigurationBootstrap bootstrap = new WebbitBootstrap(scanningUrls);
        ResteasyDeployment deployment = bootstrap.createDeployment();
        deployment.start();
        dispatcher = deployment.getDispatcher();
    }

    @Override
    public void handleHttpRequest(final HttpRequest request, final HttpResponse response, final HttpControl control) throws Exception {
        ResteasyRequest req = wrap(request);
        org.jboss.resteasy.spi.HttpResponse res = new ReasteasyResponse(response);
        dispatcher.invoke(req, res);
        response.end();
    }
}
