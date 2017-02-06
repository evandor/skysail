package io.skysail.server.http.jetty.internal;

import java.io.IOException;

import javax.servlet.ServletException;

import org.eclipse.jetty.server.HttpChannel;
import org.eclipse.jetty.server.Server;

import io.skysail.server.http.jetty.JettyServerHelper;

public class ServerDecorator extends org.eclipse.jetty.server.Server {

    private Server server;
    private JettyServerHelper helper;

    /**
     * Constructor.
     *
     * @param server
     *            The Jetty HTTP server.
     * @param jettyServerHelper
     * @param threadPool
     *            The thread pool.
     */
    public ServerDecorator(org.eclipse.jetty.server.Server server, JettyServerHelper jettyServerHelper) {
        this.server = server;
        this.helper = jettyServerHelper;
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

    @Override
    public boolean isFailed() {
        return server.isFailed();
    }

    @Override
    public boolean isStarting() {
        return server.isStarting();
    }

    @Override
    public boolean isRunning() {
        // TODO Auto-generated method stub
        return super.isRunning();
    }

    @Override
    public boolean isStarted() {
        // TODO Auto-generated method stub
        return super.isStarted();
    }

    @Override
    public boolean isStopped() {
        // TODO Auto-generated method stub
        return super.isStopped();
    }

    @Override
    public boolean isStopping() {
        // TODO Auto-generated method stub
        return super.isStopping();
    }

    @Override
    public boolean isDumpAfterStart() {
        // TODO Auto-generated method stub
        return super.isDumpAfterStart();
    }
    @Override
    public boolean isDumpBeforeStop() {
        // TODO Auto-generated method stub
        return super.isDumpBeforeStop();
    }
    @Override
    public boolean isManaged(Object bean) {
        // TODO Auto-generated method stub
        return super.isManaged(bean);
    }

    @Override
    public void clearAttributes() {
        // TODO Auto-generated method stub
        super.clearAttributes();
    }


}