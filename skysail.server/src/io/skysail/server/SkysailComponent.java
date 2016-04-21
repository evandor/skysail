package io.skysail.server;

import org.restlet.Client;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.ext.slf4j.Slf4jLoggerFacade;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkysailComponent extends Component {

    public SkysailComponent() {
        log.debug("Creating Restlet Component: {}", SkysailComponent.class.getName());

        System.out.println(getClients());
        
        getClients().add(Protocol.CLAP);

        Client httpClient = getClients().add(Protocol.HTTP);
        httpClient.getContext().getParameters().add("requestBufferSize", "16384000");
        httpClient.getContext().getParameters().add("responseBufferSize", "16384000");
        
        Client httpsClient = getClients().add(Protocol.HTTPS);
        httpsClient.getContext().getParameters().add("requestBufferSize", "16384000");
        httpsClient.getContext().getParameters().add("responseBufferSize", "16384000");

        getClients().add(Protocol.FILE);
        
        //getClients().getContext().getParameters().add("requestBufferSize", "163840");
        
        Engine.getInstance().setLoggerFacade(new Slf4jLoggerFacade());
    }
}
