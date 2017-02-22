package io.skysail.doc.swagger;

import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonInclude(Include.NON_NULL)
public class SwaggerProperty {

    private String type = "string";
    private String description = "default swagger property description";


    public SwaggerProperty(Field f) {
        io.skysail.domain.html.Field domainFieldAnnotation = f.getDeclaredAnnotation(io.skysail.domain.html.Field.class);
        //this.type = "string";//f.getType().getSimpleName().toLowerCase();
        this.description = domainFieldAnnotation.description();   
    }

}
