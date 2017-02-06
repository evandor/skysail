package io.skysail.server.http.jetty.internal;

import java.io.IOException;

import javax.servlet.ServletException;

import org.eclipse.jetty.server.HttpChannel;
import org.eclipse.jetty.util.thread.ThreadPool;

import io.skysail.server.http.jetty.JettyServerHelper;

public class WrappedServer extends org.eclipse.jetty.server.Server {
    private final JettyServerHelper helper;

    /**
     * Constructor.
     *
     * @param server
     *            The Jetty HTTP server.
     * @param threadPool
     *            The thread pool.
     */
    public WrappedServer(JettyServerHelper server, ThreadPool threadPool) {
        super(threadPool);
        this.helper = server;
    }

    /**
     * Handler method converting a Jetty HttpChannel into a Restlet Call.
     *
     * @param channel
     *            The channel to handle.
     */
    @Override
    public void handle(HttpChannel<?> channel) throws IOException,
            ServletException {
        super.handle(channel);
       /* try {
            helper.handle(new JettyServerCall(helper.getHelped(), channel));
        } catch (Throwable e) {
            channel.getEndPoint().close();
            throw new IOException("Restlet exception", e);
        }*/
    }

    @Override
    public void handleAsync(HttpChannel<?> channel) throws IOException,
            ServletException {
        // TODO: should we handle async differently?
        try {
            helper.handle(new JettyServerCall(helper.getHelped(), channel));
        } catch (Throwable e) {
            channel.getEndPoint().close();
            throw new IOException("Restlet exception", e);
        }
    }
}