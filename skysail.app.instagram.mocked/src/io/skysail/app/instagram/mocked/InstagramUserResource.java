package io.skysail.app.instagram.mocked;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class InstagramUserResource extends ServerResource {

	@Get()
	public Representation getResource() {
		return new JsonRepresentation("{\n"+
			"\"data\": {\n"+
	        "  \"id\": \"1574083\",\n"+
	        "  \"username\": \"snoopdogg\",\n"+
	        "  \"full_name\": \"Snoop Dogg\",\n"+
	        "  \"profile_picture\": \"http://distillery.s3.amazonaws.com/profiles/profile_1574083_75sq_1295469061.jpg\",\n"+
	        "  \"bio\": \"This is my bio\",\n"+
	        "  \"website\": \"http://snoopdogg.com\",\n"+
	        "  \"counts\": {\n"+
	        "    \"media\": 1320,\n"+
	        "    \"follows\": 420,\n"+
	        "    \"followed_by\": 3410\n"+
	        "  }\n"+
			"}}");
	}
	
}
