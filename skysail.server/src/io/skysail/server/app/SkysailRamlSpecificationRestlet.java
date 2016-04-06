package io.skysail.server.app;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.ext.raml.RamlSpecificationRestlet;

public class SkysailRamlSpecificationRestlet extends RamlSpecificationRestlet {

	public SkysailRamlSpecificationRestlet(Context context, SkysailApplication skysailApplication) {
		super(context);
		setApiInboundRoot(skysailApplication);
		setBasePath("http://localhost:2018/");
		setApiVersion("v33");
	}
	
	@Override
	public void handle(Request request, Response response) {
		super.handle(request, response);
	}

}
