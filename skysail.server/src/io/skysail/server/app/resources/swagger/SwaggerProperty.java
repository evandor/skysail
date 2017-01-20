package io.skysail.server.app.resources.swagger;

import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonInclude(Include.NON_NULL)
public class SwaggerProperty {

    private String type = "object";
    private String description = "desc";


    public SwaggerProperty(Field f) {
        io.skysail.domain.html.Field domainFieldAnnotation = f.getDeclaredAnnotation(io.skysail.domain.html.Field.class);
        this.type = f.getType().getName();
        this.description = domainFieldAnnotation.description();   
    }

}
