package io.skysail.osgiagent;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange; // NOSONAR

public class BundlesHandler implements com.sun.net.httpserver.HttpHandler { // NOSONAR

    @Override
    public void handle(HttpExchange http) throws IOException {
        String response = "This is the response";
        http.sendResponseHeaders(200, response.length());
        OutputStream os = http.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
