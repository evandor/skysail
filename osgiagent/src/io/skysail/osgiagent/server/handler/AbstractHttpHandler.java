package io.skysail.osgiagent.server.handler;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public abstract class AbstractHttpHandler {

    protected ObjectMapper mapper = new ObjectMapper();

    abstract String getResponse() throws JsonProcessingException;

    String getMimeType() {
        return "application/json";
    }

    public Response handle(IHTTPSession session) {
        String msg;
        try {
            msg = getResponse();
            return fi.iki.elonen.NanoHTTPD.newFixedLengthResponse(Status.OK, NanoHTTPD.MIME_HTML, msg);
        } catch (IOException e) {
            e.printStackTrace();
            return fi.iki.elonen.NanoHTTPD.newFixedLengthResponse(Status.INTERNAL_ERROR, NanoHTTPD.MIME_HTML,
                    e.getMessage());
        }
    }

}
