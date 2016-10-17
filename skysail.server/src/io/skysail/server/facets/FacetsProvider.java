package io.skysail.server.facets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;

import io.skysail.server.domain.jvm.FieldFacet;
import lombok.extern.slf4j.Slf4j;

@Component(
    immediate=true,
    configurationPolicy = ConfigurationPolicy.REQUIRE,
    configurationPid = "facets",
    service = FacetsProvider.class
)
@Slf4j
public class FacetsProvider {

    private Map<String, FieldFacet> facets = new HashMap<>();

    @Activate
    public synchronized void activate(Map<String, String> config) {
        createFacets(config);
    }

    @Deactivate
    public void deactivate() {
        facets = new HashMap<>();
    }

    private void createFacets(Map<String, String> config) {
        Set<String> items = config.keySet().stream()
            .filter(s -> s.lastIndexOf(".") > 0)
            .map(s -> s.substring(0,s.lastIndexOf(".")))
            .collect(Collectors.toSet());
        items.stream().forEach(s -> handleFacetConfig(config, s));
    }

    public FieldFacet getFacetFor(String string) {
        log.info("getting facet for id '{}'",string);
        return facets.get(string);
    }

    private void handleFacetConfig(Map<String, String> config, String ident) {
        String typeAsString = config.get(ident + ".TYPE");
        if (typeAsString == null && (
        		("felix.fileinstall".equals(ident)) || ("component".equals(ident)) || ("service".equals(ident)))
        	) { 
        	return;
        }
        if (typeAsString == null) {
            log.warn("could not find TYPE definition for facet {}", ident);
            return;
        }
        try {
            FacetType type = FacetType.valueOf(typeAsString);
            facets.put(ident, type.fromConfig(ident, getSubConfig(config, ident)));
        } catch (Exception e) {
            log.error("unable to create FacetType from '"+ident+"'", e);
        }
    }

    private Map<String, String> getSubConfig(Map<String, String> config, String s) {
        return config.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(s))
            .collect(
                Collectors.toMap(
                        e -> e.getKey().substring(1+s.length()),
                        e -> e.getValue()
                ));
    }

}
