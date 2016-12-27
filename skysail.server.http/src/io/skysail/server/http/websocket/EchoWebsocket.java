//package io.skysail.server.http.websocket;
//
//import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
//import org.eclipse.jetty.websocket.api.annotations.WebSocket;
//
//import lombok.extern.slf4j.Slf4j;
//
//@WebSocket
//@Slf4j
//public class EchoWebsocket {
//    
//    public EchoWebsocket() {
//        log.info("setting up echo Websocket");
//    }
//    
//    @OnWebSocketMessage
//    public void onText(Session session, String message) {
//        if (session.isOpen()) {
//            System.out.printf("Echoing back message [%s]%n", message);
//            session.getRemote().sendString(message, null);
//        }
//    }
//}