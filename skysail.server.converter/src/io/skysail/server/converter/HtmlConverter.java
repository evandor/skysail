package io.skysail.server.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.restlet.data.MediaType;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.api.um.UserManagementProvider;
import io.skysail.core.app.SkysailApplication;
import io.skysail.core.app.SkysailApplicationService;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.server.EventHelper;
import io.skysail.server.converter.impl.Notification;
import io.skysail.server.converter.impl.StringTemplateRenderer;
import io.skysail.server.filter.FilterParser;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.services.DefaultInstallationProvider;
import io.skysail.server.services.InstallationProvider;
import io.skysail.server.services.OsgiConverterHelper;
import io.skysail.server.services.StringTemplateProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * A component providing converting functionality via the StringTemplate
 * library.
 *
 */
@Component(immediate = true, property = { "event.topics=" + EventHelper.GUI +"/*"})
@Slf4j
public class HtmlConverter extends ConverterHelper implements OsgiConverterHelper, EventHandler {

    private static final float DEFAULT_MATCH_VALUE = 0.5f;
    private static Map<MediaType, Float> mediaTypesMatch = new HashMap<>();

    @Getter
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private volatile UserManagementProvider userManagementProvider;

    @Getter
    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile FilterParser filterParser;

    @Getter
    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile InstallationProvider installationProvider = new DefaultInstallationProvider();
    
    @Getter
    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private volatile List<StringTemplateProvider> templateProvider = new ArrayList<>();
    
    @Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
    private volatile SkysailApplicationService skysailApplicationService;

    private volatile Set<MenuItemProvider> menuProviders = new HashSet<>();

    private String templateNameFromCookie;
    private List<Event> events = new CopyOnWriteArrayList<>();
    private List<Event> peityBarEvents= new CopyOnWriteArrayList<>();


    static {
        mediaTypesMatch.put(MediaType.TEXT_HTML, 0.95F);
        mediaTypesMatch.put(SkysailApplication.SKYSAIL_TREE_FORM, 1.0F);
        mediaTypesMatch.put(SkysailApplication.SKYSAIL_TIMELINE_MEDIATYPE, 1.0F);
        mediaTypesMatch.put(SkysailApplication.SKYSAIL_STANDLONE_APP_MEDIATYPE, 1.0F);
    }

    // --- Menu Providers ------------------------------------------------

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    public void addMenuProvider(MenuItemProvider provider) {
        if (provider == null) { 
            return;
        }
        menuProviders.add(provider);
    }

    public void removeMenuProvider(MenuItemProvider provider) {
        menuProviders.remove(provider);
    }

    public Set<MenuItemProvider> getMenuProviders() {
        return Collections.unmodifiableSet(menuProviders);
    }

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        throw new RuntimeException("getObjectClasses method is not implemented yet");
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        return Arrays.asList(
                new VariantInfo(SkysailApplication.SKYSAIL_TREE_FORM),
                new VariantInfo(SkysailApplication.SKYSAIL_MAILTO_MEDIATYPE),
                new VariantInfo(SkysailApplication.SKYSAIL_TIMELINE_MEDIATYPE),
                new VariantInfo(SkysailApplication.SKYSAIL_STANDLONE_APP_MEDIATYPE)
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
        StringTemplateRenderer stringTemplateRenderer = new StringTemplateRenderer(this, resource);
        stringTemplateRenderer.setMenuProviders(menuProviders);
        stringTemplateRenderer.setFilterParser(filterParser);
        stringTemplateRenderer.setInstallationProvider(installationProvider);
        stringTemplateRenderer.setSkysailApplicationService(skysailApplicationService);

        return stringTemplateRenderer.createRepresenation((SkysailResponse<?>)skysailResponse, target, (SkysailServerResource<?>) resource);
    }

    public boolean isDebug() {
        return "debug".equalsIgnoreCase(templateNameFromCookie);
    }

    public boolean isEdit() {
        return "edit".equalsIgnoreCase(templateNameFromCookie);
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getTopic().startsWith(EventHelper.GUI_MSG)) {
            events.add(event);
        } else if (event.getTopic().startsWith(EventHelper.GUI_PEITY_BAR)) {
            peityBarEvents.add(event);
        }
    }

    public List<Notification> getNotifications() {
        Object principal = null;
        if (principal == null) {
            return new ArrayList<>();
        }
        String currentUser = principal.toString();
        if (currentUser == null) {
            return Collections.emptyList();
        }
        List<Notification> result = new ArrayList<>();
        events.stream().forEach(e -> {
            String msg = (String) e.getProperty("msg");
            String type = (String) e.getProperty("type");
            result.add(new Notification(msg, type));
            events.remove(e);
        });
        return result;
    }

    public List<String> getPeitybars() {
        List<String> result = new ArrayList<>();
        peityBarEvents.stream().forEach(e -> {
            String msg = (String) e.getProperty("msg");
            result.add(msg);
            //events.remove(e);
        });
        return result;
    }

    public String getProductName() {
        return "Skysail";
    }

}
