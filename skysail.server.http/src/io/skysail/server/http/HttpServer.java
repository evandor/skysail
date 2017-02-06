package io.skysail.server.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.resource.ServerResource;
import org.restlet.service.ConverterService;

import io.skysail.server.SkysailComponent;
import io.skysail.server.app.SkysailComponentProvider;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.http.websocket.EchoServlet;
import io.skysail.server.services.InstallationProvider;
import io.skysail.server.services.OsgiConverterHelper;
import io.skysail.server.services.RestletServicesProvider;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Component(
        immediate = true,
        configurationPolicy = ConfigurationPolicy.OPTIONAL,
        configurationPid = "skysailserver",
        property = { "event.topics=de/twenty11/skysail/server/configuration/UPDATED" }
)
@Designate(ocd = ServerConfig.class)
@Slf4j
public class HttpServer extends ServerResource
        implements RestletServicesProvider, SkysailComponentProvider, InstallationProvider {

    private static SkysailComponent restletComponent;
    private static Server server;

    private volatile ComponentContext componentContext;
    private volatile static boolean serverActive = false;
    private volatile SkysailRootApplication defaultApplication;
    private volatile List<ConverterHelper> registeredConverters;
    private int runningOnPort;
    @Getter
    private String productName;

    public HttpServer() {
        Engine.setRestletLogLevel(Level.ALL);
        removeDefaultJacksonConverter();
        registeredConverters = Engine.getInstance().getRegisteredConverters();
    }

    /**
     * We add an overwritten implementation of the jackson converter, see @link
     * {@link SkysailJacksonConverter}
     */
    private void removeDefaultJacksonConverter() {
        List<ConverterHelper> converters = Engine.getInstance().getRegisteredConverters().stream().filter(converter -> {
            return !(converter instanceof JacksonConverter);
        }).collect(Collectors.toList());
        Engine.getInstance().setRegisteredConverters(converters);

    }

    @Activate
    public void activate(ServerConfig serverConfig, ComponentContext componentContext) {
        log.debug("Activating {}", this.getClass().getName());
        this.componentContext = componentContext;
        if (restletComponent == null) {
            restletComponent = new SkysailComponent();
        }

        configure(serverConfig);

        log.debug("Started with system properties:");
        log.debug("===============================");
        System.getProperties().entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().toString().compareTo(e2.getKey().toString())).forEach(entry -> {
                    Object value = entry.getValue();
                    log.debug("  {} => '{}'", entry.getKey(), value == null ? "<null>" : value.toString());
                });
    }

    @Deactivate
    protected void deactivate(ComponentContext ctxt) {
        log.debug("Deactivating {}", this.getClass().getName());

        serverActive = false;
        try {
            if (server != null) {
                server.stop();
            }
        } catch (Exception e) {
            log.error("Exception when trying to stop standalone server", e);
        }

        restletComponent.getDefaultHost().detach(defaultApplication);

        restletComponent.setServers(null);

        try {
            restletComponent.stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        this.componentContext = null;
    }

    // --- OsgiConverterHelper
    // ------------------------------------------------------

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public synchronized void addConverterHelper(OsgiConverterHelper converterHelper) {
        if (converterHelper instanceof ConverterHelper) {
            this.registeredConverters.add((ConverterHelper) converterHelper);
            log.debug("(+ Converter)   (#{}) with name '{}'", formatSize(registeredConverters),
                    converterHelper.getClass().getName());
        }
    }

    public synchronized void removeConverterHelper(OsgiConverterHelper converterHelper) {
        if (converterHelper instanceof ConverterHelper) {
            this.registeredConverters.remove(converterHelper);
            log.debug("(- Converter)   name '{}', count is {} now", registeredConverters.getClass().getName(),
                    formatSize(registeredConverters));
        }
    }

    public void configurationChanged() {
        if (!serverActive) {
            return;
        }
        deactivate(componentContext);
    }

    @Override
    public ConverterService getConverterSerivce() {
        return defaultApplication.getConverterService();
    }

    private void configure(ServerConfig serverConfig) {
        log.debug("configuration was provided");
        runningOnPort = serverConfig.port();
        productName = serverConfig.productName();
        if (!serverActive) {
            if (serverConfig.port() == 0) {
                runningOnPort = findAvailablePort();
            }
            startHttpServer(runningOnPort);
        }

    }

    @Override
    public int getPort() {
        return runningOnPort;
    }

    private int findAvailablePort() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(0);
            return socket.getLocalPort();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
        return 2015;
    }

    @Override
    public SkysailComponent getSkysailComponent() {
        return restletComponent;
    }

    private static void startHttpServer(int port) {
        log.info("");
        log.info("====================================");
        log.info("Starting skysail server on port {}", port);
        log.info("====================================");
        log.info("");

        if (server == null) {
            try {
                server = new Server(new Context(), Protocol.HTTP, Integer.valueOf(port), restletComponent);
                server.getContext().getParameters().add("useForwardedForHeader", "true");
                // server.getContext().getParameters().add("requestBufferSize",
                // "163840");
                // server.getContext().getParameters().add("spdy.version", "2");
                // server.getContext().getParameters().add("spdy.pushStrategy",
                // "referrer");
                // server.getContext().getParameters().add("tracing", "true");
                //server.getProtocols().add(Protocol.)

                //ServletHandler handler = new ServletHandler();
                //server.setHandler(handler);

                ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
                context.setContextPath("/jetty");
                context.addServlet(EchoServlet.class.getName(), "/echo");
               // context.addServlet(EchoWebsocketServlet.class.getName(), "/ws/echo");



//                ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//                context.setContextPath("/echo/");
//                context.addServlet(EchoServlet.class.getName(), "/test");
//                //context.addServlet(EchoWebsocketServlet.class.getName(), "/ws/echo");
//
//                ContextHandlerCollection contexts = new ContextHandlerCollection();
//                contexts.setHandlers(new Handler[] { context });

//                JettyServerHelper helper = (JettyServerHelper) server.getContext().getAttributes().get("org.restlet.engine.helper");
//
//                Method getWrappedServer = JettyServerHelper.class.getDeclaredMethod("getWrappedServer");
//                getWrappedServer.setAccessible(true);
//                org.eclipse.jetty.server.Server jettyServer = (org.eclipse.jetty.server.Server) getWrappedServer.invoke(helper);
//
//                jettyServer.setHandler(context);

                server.start();


            } catch (Exception e) {
                log.error("Exception when starting standalone server trying to parse provided port (" + port + ")", e);
            }
        } else {
            try {
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (server == null) {
            throw new ComponentException("skysail server could not be started");
        }

        serverActive = true;

    }

    private String formatSize(@NonNull List<?> list) {
        return new DecimalFormat("00").format(list.size());
    }

}
