package io.skysail.doc.swagger;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Getter
@Slf4j
@JsonInclude(Include.NON_NULL)
public class SwaggerDefinition {

    private String type = "object";

    private Map<String, SwaggerProperty> properties = new HashMap<>();

    public SwaggerDefinition(Class<?> entity) {
        try {
            Object newInstance = entity.newInstance();
            List<Field> fields = Arrays.asList(newInstance.getClass().getDeclaredFields());
            fields.stream()
                .filter(f -> f.getDeclaredAnnotation(io.skysail.domain.html.Field.class) != null)
                .forEach(f -> properties.put(f.getName(), new SwaggerProperty(f)));
            
//            io.skysail.domain.html.Field declaredAnnotation = newInstance.getClass().getDeclaredAnnotation(io.skysail.domain.html.Field.class);
            
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }


}
