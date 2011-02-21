package org.webbitserver.rest.resteasy;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.plugins.server.servlet.ConfigurationBootstrap;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

import java.net.URL;

import static org.webbitserver.rest.resteasy.ResteasyRequest.wrap;

public class ResteasyHandler implements HttpHandler {
    private Dispatcher dispatcher;

    public ResteasyHandler(URL[] scanningUrls) {
        ConfigurationBootstrap bootstrap = new WebbitBootstrap(scanningUrls);
        ResteasyDeployment deployment = bootstrap.createDeployment();
        deployment.start();
        dispatcher = deployment.getDispatcher();
    }

    public ResteasyHandler(Class clazz) {
        this(new URL[]{clazz.getProtectionDomain().getCodeSource().getLocation()});
    }

    @Override
    public void handleHttpRequest(final HttpRequest request, final HttpResponse response, final HttpControl control) throws Exception {
        ResteasyRequest req = wrap(request);
        org.jboss.resteasy.spi.HttpResponse res = new ReasteasyResponse(response);
        dispatcher.invoke(req, res);
        response.end();
    }
}
