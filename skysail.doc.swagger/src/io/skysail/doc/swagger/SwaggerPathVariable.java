package io.skysail.doc.swagger;

import io.skysail.server.restlet.RouteBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SwaggerPathVariable {
    
    @Getter
    private String id = "path";
    
    @Getter
    private String name, description = "default swagger path variable description";

    public SwaggerPathVariable(RouteBuilder routeBuilder, String parameterName) {
        this.name = parameterName;
    }

}
