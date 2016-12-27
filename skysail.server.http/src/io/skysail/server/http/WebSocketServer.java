package io.skysail.server.http;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import io.skysail.server.http.websocket.EchoWebsocket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component(immediate = true)
public class WebSocketServer extends WebSocketServlet {
    
    public WebSocketServer() {
        log.info("starting webSocket Server without args");
    }

    @Override
    public void configure(WebSocketServletFactory arg0) {
        log.info("starting webSocket Server with args {}", arg0);
        arg0.register(EchoWebsocket.class);
    }

}
