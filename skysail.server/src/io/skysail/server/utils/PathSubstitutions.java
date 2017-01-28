package io.skysail.server.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.restlet.data.Reference;

import io.skysail.domain.Entity;
import io.skysail.server.restlet.RouteBuilder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString(of = "result")
public class PathSubstitutions {

    @Getter
    private String idVariable = "id";
    
    private Map<String, String> result = new HashMap<>();
    private List<String> pathVariables;

    public PathSubstitutions(@NonNull Map<String, Object> requestAttributes, @NonNull List<RouteBuilder> routeBuilders) {
        addStringValuesFromRequestAttributes(requestAttributes);
        determinePathVariables(requestAttributes, routeBuilders);
    }
    
    public Map<String, String> getFor(Object object) {
        if (!(object instanceof Entity)) {
            return result;
        }
        Entity identifiable = (Entity) object;
        if (identifiable.getId() != null) {
            if (pathVariables.size() == 1) {
                idVariable = pathVariables.get(0);
            }
            result.put(idVariable, identifiable.getId().replace("#", ""));
        }
        return result;
    }

    private void addStringValuesFromRequestAttributes(Map<String, Object> requestAttributes) {
        requestAttributes.entrySet().stream().forEach(entry -> {
            if (entry.getValue() instanceof String) {
                result.put(entry.getKey(), Reference.decode((String)entry.getValue()));
            }
        });
    }
    
    private void determinePathVariables(Map<String, Object> requestAttributes, List<RouteBuilder> routeBuilders) {
        pathVariables = routeBuilders.stream().map(builder -> builder.getPathVariables()).flatMap(pv -> pv.stream()).collect(Collectors.toList());

        pathVariables.removeIf(pathVariable -> {
            if (requestAttributes.get(pathVariable) != null) {
                result.put(pathVariable, (String)requestAttributes.get(pathVariable));
                return true;
            }
            return false;
        });
    }



}
