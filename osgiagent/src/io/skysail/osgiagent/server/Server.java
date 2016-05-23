package io.skysail.osgiagent.server;

import java.io.IOException;

import org.osgi.framework.BundleContext;

import fi.iki.elonen.NanoHTTPD;
import io.skysail.osgiagent.listener.AgentBundleListener;
import io.skysail.osgiagent.listener.AgentServiceListener;
import io.skysail.osgiagent.server.handler.BundleListenerHandler;
import io.skysail.osgiagent.server.handler.BundlesHandler;
import io.skysail.osgiagent.server.handler.ServiceListenerHandler;
import io.skysail.osgiagent.server.handler.StaticFilesHandler;

public class Server extends NanoHTTPD {

    private BundleContext bundleContext;
    private StaticFilesHandler staticFilesHandler;
    private AgentBundleListener bundleListener;
    private AgentServiceListener serviceListener;
    private BundleListenerHandler bundleListenerHandler;
    private BundlesHandler bundlesHandler;
    private ServiceListenerHandler serviceListenerHandler;

    public Server(BundleContext bundleContext) throws IOException {
        super(2002);
        this.bundleContext = bundleContext;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);

        bundleListener = new AgentBundleListener(bundleContext);
        serviceListener = new AgentServiceListener(bundleContext);

        bundlesHandler = new BundlesHandler(bundleContext);
        bundleListenerHandler = new BundleListenerHandler(bundleListener);
        serviceListenerHandler = new ServiceListenerHandler(serviceListener);
        staticFilesHandler = new StaticFilesHandler(bundleContext.getBundle());

    }

    @Override
    public Response serve(IHTTPSession session) {
        if ("/osgi/bundles".equals(session.getUri())) {
            return bundlesHandler.handle(session);
        }
        if ("/osgi/bundlelistener".equals(session.getUri())) {
            return bundleListenerHandler.handle(session);
        }
        if ("/osgi/servicelistener".equals(session.getUri())) {
            return serviceListenerHandler.handle(session);
        }
        if (session.getUri().startsWith("/osgi")) {
            return staticFilesHandler.handle(session);
        }
        return newFixedLengthResponse("no route matched");
    }
}