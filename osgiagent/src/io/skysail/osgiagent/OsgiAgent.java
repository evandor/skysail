package io.skysail.osgiagent;

import java.net.InetSocketAddress;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import lombok.extern.slf4j.Slf4j;

@Component(immediate = true)
@Slf4j
public class OsgiAgent {

    private BundleContext bundleContext;
    private Thread loggerThread;
    private com.sun.net.httpserver.HttpServer server; // NOSONAR

    @Activate
    public void activate(ComponentContext ctx) {
        bundleContext = ctx.getBundleContext();
        startAgent();
    }

    @Deactivate
    public void deactivate() {
        bundleContext = null;
        try {
            server.stop(0);
            loggerThread.interrupt();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void startAgent() {
        loggerThread = new Thread(this::createServer);
        loggerThread.start();
    }

    private void createServer() { // NOSONAR
        try {
            server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(2002), 0); // NOSONAR
            server.createContext("/osgi/bundles", new BundlesHandler());
            server.createContext("/osgi", new StaticFilesHandler(bundleContext.getBundle()));
            server.setExecutor(null);
            server.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
