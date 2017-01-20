package io.skysail.server.app.resources.swagger;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.ServerResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Getter
@Slf4j
@JsonInclude(Include.NON_NULL)
public class SwaggerPath {

    private Map<String, Object> get;
    private Map<String, Object> post;

    public SwaggerPath(RouteBuilder routeBuilder) {
        Class<?> parentClass = routeBuilder.getTargetClass().getSuperclass();
        if (EntityServerResource.class.isAssignableFrom(parentClass)) {
            get = initIfNeccessary(get);
            get.put("responses", addGet(routeBuilder));
        } else if (PostEntityServerResource.class.isAssignableFrom(parentClass)) {
            get = initIfNeccessary(get);
            post = initIfNeccessary(post);
            get.put("responses", addGet(routeBuilder));
            get.put("responses", addPost(routeBuilder));
        }
    }

    private Map<String, Object> addGet(RouteBuilder routeBuilder) {

        get.put("description", "desc");
        get.put("produces", Arrays.asList("application/json"));
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("200", new HashMap<String, String>() {
            {
                put("description", "post entity get");
            }
        });
        return responseMap;
    }

    private Map<String, Object> addPost(RouteBuilder routeBuilder) {
        post.put("summary", "POST endpoint defined in " + routeBuilder.getTargetClass().getSimpleName());
        analyse(routeBuilder.getTargetClass());
        post.put("produces", Arrays.asList("application/json"));
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("200", new HashMap<String, String>() {
            {
                put("description", "post entity get");
            }
        });
        return responseMap;
    }

    private void analyse(Class<? extends ServerResource> targetClass) {
        try {
            ServerResource newInstance = targetClass.newInstance();
            Method getDescriptionMethod = newInstance.getClass().getMethod("getDescription");
            post.put("description", getDescriptionMethod.invoke(newInstance));
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        
    }

    private Map<String, Object> initIfNeccessary(Map<String, Object> theMap) {
        if (theMap == null) {
            return new HashMap<>();
        }
        return theMap;
    }

}
