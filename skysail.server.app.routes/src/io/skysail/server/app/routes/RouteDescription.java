package io.skysail.server.app.routes;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.restlet.RouteBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RouteDescription implements Identifiable {
    
    private String id;
    
    @Field(inputType = InputType.URL)
    private String pathTemplate;

    @Field
    private String appName;

    @Field
    private String target;

	public RouteDescription(String appName, String path, RouteBuilder routeBuilder) {
	    this.appName = appName;
        pathTemplate = "/" + appName + path;
	    this.id = pathTemplate;
	    this.target = routeBuilder.getTargetClass() != null ? routeBuilder.getTargetClass().getName() : "";
    }


}