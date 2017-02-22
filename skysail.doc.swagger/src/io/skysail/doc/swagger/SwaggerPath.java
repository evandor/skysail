package io.skysail.doc.swagger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.resource.ServerResource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.skysail.core.resources.SkysailServerResource;
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

    private static final String PARAMETERS = "parameters";
    private static final String RESPONSES = "responses";
    private static final String PRODUCES = "produces";
    private static final String DESCRIPTION = "description";

    private Map<String, Object> get;
    private Map<String, Object> post;

    public SwaggerPath(RouteBuilder routeBuilder) {
        Class<?> parentClass = routeBuilder.getTargetClass().getSuperclass();

        SkysailServerResource<?> newInstance;
		try {
			newInstance = (SkysailServerResource<?>) routeBuilder.getTargetClass().newInstance();
			Map<org.restlet.data.Method, Map<String, Object>> apiMetadata = newInstance.getApiMetadata();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (EntityServerResource.class.isAssignableFrom(parentClass)) {
            get = initIfNeccessary(get);
            get.put(DESCRIPTION, "default swagger get path description");
            get.put(PRODUCES, Arrays.asList("application/json"));
            if (!routeBuilder.getPathVariables().isEmpty()) {
                List<SwaggerParameter> parameterList = new ArrayList<>();
                routeBuilder.getPathVariables().stream().forEach(path -> parameterList.add(new SwaggerParameter(path)));
                get.put(PARAMETERS, parameterList);
            }
            get.put(RESPONSES, addGetResponses(routeBuilder));
        } else if (PostEntityServerResource.class.isAssignableFrom(parentClass)) {
            get = initIfNeccessary(get);
            post = initIfNeccessary(post);

            get.put(DESCRIPTION, "default swagger get path description");
            get.put(PRODUCES, Arrays.asList("application/json"));
            //get.put(PARAMETERS, new SwaggerParameter(routeBuilder));
            get.put(RESPONSES, addGetResponses(routeBuilder));

            post.put(DESCRIPTION, "default swagger post path description");
            post.put(PRODUCES, Arrays.asList("application/json"));
            post.put(RESPONSES, addPostResponse(routeBuilder));
        }
    }

    private Map<String, Object> addGetResponses(RouteBuilder routeBuilder) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("200", new HashMap<String, String>() {{ put(DESCRIPTION, "post entity get"); }});
        return responseMap;
    }

    private Map<String, Object> addPostResponse(RouteBuilder routeBuilder) {
        post.put("summary", "POST endpoint defined in " + routeBuilder.getTargetClass().getSimpleName());
        analyse(routeBuilder.getTargetClass());
        post.put(PRODUCES, Arrays.asList("application/json"));
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("200", new HashMap<String, String>() {{ put(DESCRIPTION, "post entity get"); }});
        return responseMap;
    }

    private void analyse(Class<? extends ServerResource> targetClass) {
        try {
            ServerResource newInstance = targetClass.newInstance();
            Method getDescriptionMethod = newInstance.getClass().getMethod("getDescription");
            String desc = (String) getDescriptionMethod.invoke(newInstance);
            post.put(DESCRIPTION, desc != null ? desc : "");
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
