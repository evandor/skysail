package io.skysail.server.app.resources.swagger;

import io.skysail.server.restlet.RouteBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SwaggerPathVariable {
    
    @Getter
    private String id = "path";
    
    @Getter
    private String name, description = "desc";

    public SwaggerPathVariable(RouteBuilder routeBuilder, String parameterName) {
        this.name = parameterName;
    }

}