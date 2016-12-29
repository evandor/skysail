package io.skysail.server.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.restlet.data.MediaType;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.services.OsgiConverterHelper;
import io.skysail.server.services.StringTemplateProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Support for Carbon-I18n
 */
@Component(immediate = true)
@Slf4j
public class CarbonI18nJsConverter extends ConverterHelper implements OsgiConverterHelper {

    private static final float DEFAULT_MATCH_VALUE = 0.5f;
    private static Map<MediaType, Float> mediaTypesMatch = new HashMap<>();

    @Getter
    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private volatile List<StringTemplateProvider> templateProvider = new ArrayList<>();

    private String templateNameFromCookie;


    static {
        mediaTypesMatch.put(SkysailApplication.SKYSAIL_CARBON_I18N_JS, 1.0F);
    }

    // --- Menu Providers ------------------------------------------------

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        throw new RuntimeException("getObjectClasses method is not implemented yet");
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        return Arrays.asList(
                new VariantInfo(SkysailApplication.SKYSAIL_CARBON_I18N_JS)
        );
    }

    @Override
    public float score(Object source, Variant target, Resource resource) {
        if (target == null) {
            return 0.0f;
        }
        for (MediaType mediaType : mediaTypesMatch.keySet()) {
            if (target.getMediaType().equals(mediaType)) {
                log.debug("converter '{}' matched '{}' with threshold {}", new Object[] {
                        this.getClass().getSimpleName(), mediaTypesMatch, mediaTypesMatch.get(mediaType) });
                return mediaTypesMatch.get(mediaType);
            }
        }
        return DEFAULT_MATCH_VALUE;
    }

    @Override
    public <T> float score(Representation source, Class<T> target, Resource resource) {
        return -1.0F;
    }

    @Override
    public <T> T toObject(Representation source, Class<T> target, Resource resource) {
        throw new RuntimeException("toObject method is not implemented yet");
    }

    /**
     * source: List of entities variant: e.g. [text/html] resource: ? extends
     * SkysailServerResource
     */
    @Override
    public Representation toRepresentation(Object skysailResponse, Variant target, Resource resource) {
//        StringTemplateRenderer stringTemplateRenderer = new StringTemplateRenderer(this, resource);
//        stringTemplateRenderer.setMenuProviders(menuProviders);
//        stringTemplateRenderer.setFilterParser(filterParser);
//        stringTemplateRenderer.setInstallationProvider(installationProvider);
//
//        return stringTemplateRenderer.createRepresenation((SkysailResponse<?>)skysailResponse, target, (SkysailServerResource<?>) resource);
        StringRepresentation js = new StringRepresentation(
                "!function() {\n"+
                "    Polymer.CarbonI18nBehaviorLocales.add('sky-form', 'en', {\n"+
                "        hello: 'Hallo Welt',\n"+
                "        members: '{0} Mitglieder',\n"+
                "        intro: 'Mein Name ist {0} und ich bin {1} Jahre alt.'\n"+
                "    });\n"+
                "}();\n"
        );
        js.setMediaType(MediaType.APPLICATION_JAVASCRIPT);
        return js;
    }


}
