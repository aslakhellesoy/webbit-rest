package org.webbitserver.rest.resteasy;

import org.jboss.resteasy.plugins.server.servlet.ConfigurationBootstrap;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WebbitBootstrap extends ConfigurationBootstrap {
    private static final Map<String,String> PARAMS = new HashMap<String,String>(){{
        put("resteasy.scan", "true");
    }};
    private final URL[] scanningUrls;

    public WebbitBootstrap(URL[] scanningUrls) {
        this.scanningUrls = scanningUrls;
    }

    @Override
    public String getParameter(String key) {
        System.out.println("s = " + key);
        return PARAMS.get(key);
    }

    @Override
    public String getInitParameter(String s) {
        throw new UnsupportedOperationException(s);
    }

    @Override
    public URL[] getScanningUrls() {
        return scanningUrls;
    }
}
