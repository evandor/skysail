package io.skysail.server.app;

import java.io.IOException;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.ext.raml.RamlSpecificationRestlet;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

public class SkysailRamlSpecificationRestlet extends RamlSpecificationRestlet {

	public SkysailRamlSpecificationRestlet(Context context, SkysailApplication skysailApplication) {
		super(context);
		setApiInboundRoot(skysailApplication);
		setBasePath("http://localhost:2018/demoapp");
		setApiVersion("v33");
	}
	
	@Override
	public void handle(Request request, Response response) {
		super.handle(request, response);
		Representation ramlRepresentation = response.getEntity();
		String txt;
		try {
			txt = ramlRepresentation.getText().replace("\"\":","");
			txt = txt.replace("displayName: \"Finder with no target class: \"\n    get: ","");
			response.setEntity(new StringRepresentation(txt));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
