package io.skysail.server.restlet;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.restlet.Restlet;
import org.restlet.resource.ServerResource;

import com.google.common.base.Predicate;

import io.skysail.core.app.ApiVersion;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class RouteBuilder {

	@Getter
    private final String pathTemplate;

	@Setter
	@Getter
    private Class<? extends ServerResource> targetClass;

	@Getter
	private Restlet restlet;

    @Getter
    private List<String> pathVariables = new ArrayList<>();

    @Getter
    private boolean needsAuthentication = true; // default

    @Getter
    private Predicate<String[]> rolesForAuthorization;

    private Pattern pathVariablesPattern = Pattern.compile("\\{([^\\}])*\\}");

    public RouteBuilder(@NonNull String pathTemplate, @NonNull Class<? extends ServerResource> targetClass) {
        this.pathTemplate = pathTemplate;
        this.targetClass = targetClass;
        pathVariables = extractPathVariables(pathTemplate);
    }

    public RouteBuilder(@NonNull String pathTemplate, @NonNull Restlet restlet) {
        this.pathTemplate = pathTemplate;
        this.restlet = restlet;
        pathVariables = extractPathVariables(pathTemplate);
    }

    public String getPathTemplate(ApiVersion apiVersion) {
        if (apiVersion == null) {
            return pathTemplate;
        }
        return apiVersion.getVersionPath() + pathTemplate;
    }

    /**
     * the current user needs on of those roles to get authorized
     */
    public RouteBuilder authorizeWith(Predicate<String[]> predicate) {
        this.rolesForAuthorization = predicate;
        return this;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RouteBuilder: [\n  '");
        sb.append(pathTemplate).append("' -> ");
        if (targetClass != null) {
            sb.append(targetClass);
        }
        if (restlet != null) {
            sb.append(restlet);
        }
        sb.append("\n]");
        return sb.toString();
    }

    private List<String> extractPathVariables(String input) {
        List<String> result = new ArrayList<>();
        Matcher m = pathVariablesPattern.matcher(input);
        while (m.find()) {
            result.add(m.group(0).replace("}", "").replace("{", ""));
        }
        return result;
    }

}
