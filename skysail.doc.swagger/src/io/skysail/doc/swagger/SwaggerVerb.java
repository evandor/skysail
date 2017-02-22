package io.skysail.doc.swagger;

import org.restlet.resource.ServerResource;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SwaggerVerb {
	
	private String description = "description";

	public SwaggerVerb(Class<? extends ServerResource> targetClass) {
	}

}
