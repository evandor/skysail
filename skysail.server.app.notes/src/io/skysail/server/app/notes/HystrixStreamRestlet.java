package io.skysail.server.app.notes;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.ext.servlet.ServletUtils;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

public class HystrixStreamRestlet extends Restlet { //EntityServerResource<GenericIdentifiable> {

    @Override
    public void handle(Request req, Response res) {


//        HttpServletRequest result = new org.eclipse.jetty.server.Request();
//
//        if (req instanceof HttpRequest) {
//            Call call = ((HttpRequest) req).getHttpCall();
//
//            if (call instanceof ServerCall) {
//                result = ((ServerCall) call).
//            }
//        }


        HttpServletRequest request = org.restlet.ext.servlet.ServletUtils.getRequest(req);
        HttpServletResponse response = ServletUtils.getResponse(res);

        HystrixMetricsStreamServlet servlet = new HystrixMetricsStreamServlet();

        try {
            servlet.service(request, response);
        } catch (ServletException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
