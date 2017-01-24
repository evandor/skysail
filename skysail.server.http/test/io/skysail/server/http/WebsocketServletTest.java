//package io.skysail.server.http;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.servlet.ServletHandler;
//import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
//import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
//
//public class WebsocketServletTest {
//
//    public static void main(String[] args) throws Exception {
//
//        Server server = new Server(8080);
//
//        // The ServletHandler is a dead simple way to create a context handler
//        // that is backed by an instance of a Servlet.
//        // This handler then needs to be registered with the Server object.
//        ServletHandler handler = new ServletHandler();
//        server.setHandler(handler);
//
//        // Passing in the class for the Servlet allows jetty to instantiate an
//        // instance of that Servlet and mount it on a given context path.
//
//        // IMPORTANT:
//        // This is a raw Servlet, not a Servlet that has been configured
//        // through a web.xml @WebServlet annotation, or anything similar.
//        handler.addServletWithMapping(HelloServlet.class, "/echo/*");
//
//        // Start things up!
//        server.start();
//
//        // The use of server.join() the will make the current thread join and
//        // wait until the server is done executing.
//        // See
//        // http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()
//        server.join();
//    }
//
//    @SuppressWarnings("serial")
//    public static class HelloServlet extends WebSocketServlet {
//
//        @Override
//        public void configure(WebSocketServletFactory arg0) {
//            System.out.println("hier");
//        }
//    }
//}
