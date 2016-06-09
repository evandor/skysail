package io.skysail.server.um.shiro.web.utils;

import org.restlet.Request;
import org.restlet.Response;

import io.skysail.server.um.shiro.web.impl.RestletRequestPairSource;

/**
 * Simple utility class for operations used across multiple class hierarchies in
 * the restlet related framework code.
 *
 */
public class RestletUtils {

    public static Request getRequest(Object requestPairSource) {
        if (requestPairSource instanceof RestletRequestPairSource) {
            return ((RestletRequestPairSource) requestPairSource).getRestletRequest();
        }
        return null;
    }

    public static Response getResponse(Object requestPairSource) {
        if (requestPairSource instanceof RestletRequestPairSource) {
            return ((RestletRequestPairSource) requestPairSource).getRestletResponse();
        }
        return null;
    }

    public static boolean isRestlet(Object requestPairSource) {
        return requestPairSource instanceof RestletRequestPairSource
                && isRestlet((RestletRequestPairSource) requestPairSource);
    }

    private static boolean isRestlet(RestletRequestPairSource source) {
        Request request = source.getRestletRequest();
        Response response = source.getRestletResponse();
        return request != null && response != null;
    }

}
