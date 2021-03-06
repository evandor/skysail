package io.skysail.doc.swagger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.restlet.Request;
import org.restlet.resource.ServerResource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.skysail.core.app.SkysailApplication;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.RouteBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class SwaggerSpec implements Entity {

    @JsonIgnore
    private String id;

    private String swagger = "2.0";

    private SwaggerInfo info;

    private String host = "localhost:2021";// : petstore.swagger.io
    private final String basePath;// : /api

    private List<String> schemes = Arrays.asList("http");

    private Map<String, SwaggerPath> paths = new HashMap<>();

    private Map<String, SwaggerDefinition> definitions = new HashMap<>();

    private SwaggerExternalDoc externalDocs = new SwaggerExternalDoc("skysail documentation", "https://evandor.gitbooks.io/skysail/content/");

    @JsonIgnore
    private Set<Class<?>> types = new HashSet<>();

    public SwaggerSpec(SkysailApplication skysailApplication, Request request) {
        this.info = new SwaggerInfo(skysailApplication);
        this.host = determineHost(skysailApplication,request);
        this.basePath = "/" + skysailApplication.getName() + skysailApplication.getApiVersion().getVersionPath();
        determinePaths(skysailApplication);
        determineDefinitions(skysailApplication);
    }

    private void determinePaths(SkysailApplication skysailApplication) {
        String versionPath = skysailApplication.getApiVersion().getVersionPath();
        skysailApplication.getRoutesMap().keySet().stream()
                .sorted((p1, p2) -> p1.compareTo(p2))
                .filter(path -> path.startsWith(versionPath))
                .filter(path -> !path.startsWith(versionPath + "/_"))
                .filter(path -> path.length() > versionPath.length() + 1)
                .forEach(path -> {
                    RouteBuilder routeBuilder = skysailApplication.getRoutesMap().get(path);
                    Class<? extends ServerResource> targetClass = routeBuilder.getTargetClass();
                    if (SkysailServerResource.class.isAssignableFrom(targetClass)) {
                        try {
                            SkysailServerResource newInstance = (SkysailServerResource) targetClass.newInstance();
                            types.add(newInstance.getParameterizedType());
                        } catch (Exception e) {
                        }
                    }
                    paths.put(path.substring(versionPath.length()), new SwaggerPath(routeBuilder));
                });

    }

    private void determineDefinitions(SkysailApplication skysailApplication) {
        types.stream()
                .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                .forEach(entity -> {
                    definitions.put(entity.getSimpleName(), new SwaggerDefinition(entity));
                });
    }

    private String determineHost(SkysailApplication skysailApplication, Request request) {
        if (!skysailApplication.getHost().isEmpty()) {
            return skysailApplication.getHost();
        }
        String hostDomainAsIp = request.getHostRef().getHostDomain();
        try {
            InetAddress addr = InetAddress.getByName(hostDomainAsIp);
            String host = addr.getHostName();
            return host + ":" + request.getHostRef().getHostPort();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return request.getHostRef().getHostDomain() + ":" + request.getHostRef().getHostPort();
        }
    }

}
