package io.skysail.osgiagent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import org.osgi.framework.Bundle;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange; // NOSONAR

public class StaticFilesHandler implements com.sun.net.httpserver.HttpHandler {
    private Bundle agentBundle;

    // NOSONAR

    public StaticFilesHandler(Bundle bundle) {
        this.agentBundle = bundle;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String root = "";
        URI uri = t.getRequestURI();
        System.out.println("looking for: " + root + uri.getPath());
        String path = uri.getPath();

        URL resource = agentBundle.getResource(root + uri.getPath());

        BufferedReader br = new BufferedReader(new InputStreamReader(resource.openConnection().getInputStream()));
        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            sb.append(br.readLine()).append("\n");
        }
        br.close();

        String response = sb.toString();

        String mime = "text/html";
        if (path.substring(path.length() - 3).equals(".js"))
            mime = "application/javascript";
        if (path.substring(path.length() - 3).equals("css"))
            mime = "text/css";

        Headers h = t.getResponseHeaders();
        h.set("Content-Type", mime);
        t.sendResponseHeaders(200, 0);
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes(), 0, response.length());
        os.close();


    }

}
