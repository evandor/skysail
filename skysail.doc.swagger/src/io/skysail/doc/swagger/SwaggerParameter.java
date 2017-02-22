package io.skysail.doc.swagger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonInclude(content = Include.NON_NULL)
public class SwaggerParameter {

    @Getter
    private String in = "path";
    
    @Getter
    private String name;
    
    @Getter
    private String description = "default swagger parameter description";

    @Getter
    private Boolean required = true;

    @Getter
    private String type = "string";

    public SwaggerParameter(String name) {
        this.name = name;
    }

}
