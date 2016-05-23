package io.skysail.osgiagent;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;

import lombok.extern.slf4j.Slf4j;

//@Component(immediate = true)
@Slf4j
public class OsgiAgent {

    private BundleContext bundleContext;
    private Thread loggerThread;
    private io.skysail.osgiagent.server.Server server;

    @Activate
    public void activate(ComponentContext ctx) {
        bundleContext = ctx.getBundleContext();
        startAgent();
    }

    @Deactivate
    public void deactivate() {
        bundleContext = null;
        try {
            server.stop();
            loggerThread.interrupt();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void startAgent() {
        loggerThread = new Thread(this::createServer);
        loggerThread.start();
    }

    private void createServer() {
        try {
            server = new io.skysail.osgiagent.server.Server(bundleContext);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
