package io.skysail.server.app.resources.swagger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class SwaggerSpec implements Identifiable {

	@JsonIgnore
	private String id;
	
	private String swagger = "2.0";
	
	private SwaggerInfo info;

	private String host = "localhost";//: petstore.swagger.io
	private final String basePath;//: /api
	private List<String> schemes = Arrays.asList("http");
		
	private Map<String, SwaggerPath> paths = new HashMap<>();
	
	public SwaggerSpec(SkysailApplication skysailApplication) {
		this.info = new SwaggerInfo(skysailApplication.getApplicationModel());
		this.basePath = "/" + skysailApplication.getName() + skysailApplication.getApiVersion().getVersionPath();
		determinePaths(skysailApplication);
	}

	private void determinePaths(SkysailApplication skysailApplication) {
		skysailApplication.getRoutesMap().keySet().stream()
			.sorted((p1,p2) -> p1.compareTo(p2))
			.forEach(path -> {
				paths.put(path, new SwaggerPath(skysailApplication.getRoutesMap().get(path)));
			});
		
	}
	
}
