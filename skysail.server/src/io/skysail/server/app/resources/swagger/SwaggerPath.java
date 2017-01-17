package io.skysail.server.app.resources.swagger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.restlet.resources.EntityServerResource;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonInclude(Include.NON_NULL)
public class SwaggerPath {

	private Map<String, Object> get;
	private Map<String, Object> post;
	
	public SwaggerPath(RouteBuilder routeBuilder) {
		Class<?> parentClass = routeBuilder.getTargetClass().getSuperclass();
		if (EntityServerResource.class.isAssignableFrom(parentClass)) {
			
			if (get == null) {
				 get = new HashMap<>();
			}
			
			get.put("description", "desc");
			get.put("produces", Arrays.asList("application/json"));
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("200", new HashMap<String,String>() {{
				put("description", "desc");
			}});
			get.put("responses", responseMap);
		}
	}

}
