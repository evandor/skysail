package io.skysail.server.converter.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.api.text.Translation;
import io.skysail.core.app.SkysailApplication;
import io.skysail.core.app.SkysailApplicationService;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.core.utils.CookiesUtils;
import io.skysail.server.Constants;
import io.skysail.server.ResourceContextId;
import io.skysail.server.caches.Caches;
import io.skysail.server.converter.HtmlConverter;
import io.skysail.server.converter.wrapper.STRequestWrapper;
import io.skysail.server.converter.wrapper.STUserWrapper;
import io.skysail.server.filter.FilterParser;
import io.skysail.server.forms.FormField;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.model.ResourceModel;
import io.skysail.server.rendering.RenderingMode;
import io.skysail.server.rendering.Styling;
import io.skysail.server.rendering.Theme;
import io.skysail.server.restlet.response.messages.Message;
import io.skysail.server.services.InstallationProvider;
import io.skysail.server.utils.RequestUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Leverages StringTemplate engine to create html for resource representation.
 *
 * The templates are provided by the resources' bundle or the
 * skysail.server.converter bundle.
 *
 */
@Slf4j
public class StringTemplateRenderer {

    private static final String SKYSAIL_SERVER_CONVERTER = "skysail.server.converter";
    private static final String TEMPLATES_DIR = "/templates";
    private static final String INDEX_FOR_MOBILES = "indexMobile";

    @Setter
    protected Set<MenuItemProvider> menuProviders;

    @Setter
    protected FilterParser filterParser;

    @Setter
    protected InstallationProvider installationProvider;

    @Setter
    protected SkysailApplicationService skysailApplicationService;

    private STGroupBundleDir importedGroupBundleDir;
    protected HtmlConverter htmlConverter;
    private Resource resource;
    protected Theme theme;
    private RenderingMode mode;
    private Bundle appBundle;
    private Styling styling;

    @Getter
    private List<String> uiPolymerExtensions = new ArrayList<>();

    public StringTemplateRenderer(HtmlConverter htmlConverter, Resource resource) {
        this.htmlConverter = htmlConverter;
        this.resource = resource;
        this.appBundle = ((SkysailApplication) resource.getApplication()).getBundle();
        this.mode = CookiesUtils.getModeFromCookie(resource.getRequest());
        if (resource instanceof SkysailServerResource) {
            SkysailServerResource<?> ssr = (SkysailServerResource<?>)resource;
            String appName = resource.getApplication().getName();
            ssr.getPolymerUiExtensions().stream().forEach(entry -> {
                String name = "/"+appName+"/v1/sky-"+appName+"/" + entry.trim()+".html";
                uiPolymerExtensions.add(name);
            });
        }
    }

    public StringRepresentation createRepresenation(SkysailResponse<?> entity, Variant target,
            SkysailServerResource<?> resource) {

        styling = Styling.determineForm(resource); // e.g. bootstrap,
                                                   // semanticui, jquerymobile
        theme = Theme.determineFrom(resource, target);

        STGroupBundleDir.clearUsedTemplates();
        STGroup stGroup = createStringTemplateGroup(resource, styling, theme);
        ST index = getStringTemplateIndex(resource, styling, stGroup);

        ResourceModel<SkysailServerResource<?>, ?> resourceModel = createResourceModel(entity, target, resource);

        addSubstitutions(resourceModel, index);

        checkForInspection(resource, index);

        return createRepresentation(index, stGroup);
    }

    public List<Notification> getNotifications() {
        List<Notification> notifications = htmlConverter.getNotifications();
        String messageIds = resource.getOriginalRef().getQueryAsForm().getFirstValue("msgIds");
        if (messageIds != null) {
            List<Message> messages = Arrays.stream(messageIds.split("|"))
                    .map(this::getMessageFromCache)
                    .filter(msg -> msg != null)
                    .collect(Collectors.toList());
            for (Message message : messages) {
                notifications.add(new Notification(message.getMsg(), "success"));
            }
        }
        String errorMessage = resource.getAttribute("message.error");
        if (errorMessage != null) {
            notifications.add(new Notification(errorMessage, "error"));
        }
        return notifications;
    }

    public boolean isDebug() {
        return mode.equals(RenderingMode.DEBUG);
    }

    public boolean isEdit() {
        return mode.equals(RenderingMode.EDIT);
    }

    public String getEditBaseUrl() {
        if (mode.equals(RenderingMode.EDIT)) {
            return "/content/v1";
        }
        return null;
    }

    public List<String> getPeitybars() {
        return htmlConverter.getPeitybars();
    }

    private synchronized STGroup createStringTemplateGroup(Resource resource, Styling styling, Theme theme) {
        STGroupBundleDir stGroup = new STGroupBundleDir(determineBundleToUse(), resource, TEMPLATES_DIR,
                htmlConverter.getTemplateProvider());
        importFrom(resource, theme, stGroup, SKYSAIL_SERVER_CONVERTER);
        importFrom(resource, theme, stGroup, System.getProperty(Constants.PRODUCT_BUNDLE_IDENTIFIER));
        return stGroup;
    }

    private ST getStringTemplateIndex(Resource resource, Styling styling, STGroup stGroup) {
        if (resource instanceof SkysailServerResource
                && resource.getContext() != null
                && ((SkysailServerResource<?>) resource).getFromContext(ResourceContextId.RENDERER_HINT) != null) {
            String root = ((SkysailServerResource<?>) resource).getFromContext(ResourceContextId.RENDERER_HINT);
            resource.getContext().getAttributes().remove(ResourceContextId.RENDERER_HINT.name());
            return stGroup.getInstanceOf(root + "_index");
        }

        if (RequestUtils.isMobile(resource.getRequest())) {
            return stGroup.getInstanceOf(INDEX_FOR_MOBILES);
        }
        if (styling.getName().length() > 0) {
            ST instanceOf = stGroup.getInstanceOf(styling.getName() + "_index");
            if (instanceOf == null) {
                // fallback
                return stGroup.getInstanceOf("index");
            }
            return instanceOf;
        }

        return stGroup.getInstanceOf(getIndexPageNameFromCookie(resource).orElse("index"));

    }

    private Optional<String> getIndexPageNameFromCookie(Resource resource) {
        return CookiesUtils.getMainPageFromCookie(resource.getRequest());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected ResourceModel<SkysailServerResource<?>, ?> createResourceModel(Object entity, Variant target,
            SkysailServerResource<?> resource) {

        ResourceModel<SkysailServerResource<?>, ?> resourceModel = new ResourceModel(resource,
                (SkysailResponse<?>) entity, htmlConverter.getUserManagementProvider(), target, theme);
        resourceModel.setMenuItemProviders(menuProviders);
        resourceModel.setFilterParser(filterParser);
        resourceModel.setInstallationProvider(installationProvider);
        resourceModel.setTemplateProvider(htmlConverter.getTemplateProvider());
        resourceModel.setSkysailApplicationService(skysailApplicationService);

        resourceModel.process();

        Map<String, Translation> messages = resource.getMessages(resourceModel.getFields());
        resourceModel.setMessages(messages);

        return resourceModel;
    }

    public List<String> getLeftNavPolymerExtensions() {
        return matchingExtension("-left-nav");
    }

    public List<String> getContentPolymerExtensions() {
        return matchingExtension("-content-");
    }

    private StringRepresentation createRepresentation(ST index, STGroup stGroup) {
        String stringTemplateRenderedHtml = index.render();
        if (importedGroupBundleDir != null && stGroup instanceof STGroupBundleDir) {
            ((STGroupBundleDir) stGroup).addUsedTemplates(STGroupBundleDir.getUsedTemplates());
        }
        StringRepresentation rep = new StringRepresentation(stringTemplateRenderedHtml);
        rep.setMediaType(MediaType.TEXT_HTML);
        return rep;
    }

    private void importFrom(Resource resource, Theme theme, STGroupBundleDir stGroup, String symbolicName) {
        Optional<Bundle> theBundle = findBundle(appBundle.getBundleContext(), symbolicName);
        importTemplates(theBundle, resource, TEMPLATES_DIR, stGroup, theme);
    }

    private void addSubstitutions(ResourceModel<SkysailServerResource<?>, ?> resourceModel, @NonNull ST decl) {

        SkysailServerResource<?> resource = resourceModel.getResource();

        String installationFromCookie = CookiesUtils.getInstallationFromCookie(resource.getRequest()).orElse(null);

        decl.add("user", new STUserWrapper(htmlConverter.getUserManagementProvider(), resourceModel.getResource(),
                installationFromCookie));
        decl.add("converter", this);

        decl.add("messages", resourceModel.getMessages());
        decl.add("model", resourceModel);
        decl.add("request", new STRequestWrapper(
                resource.getRequest(),
                resourceModel.getFormfields().stream().map(FormField::getId).collect(Collectors.toList())));
    }

    private Message getMessageFromCache(String id) {
        return Caches.getMessageCache().getIfPresent(Long.valueOf(id));
    }

    private void importTemplates(Optional<Bundle> theBundle, Resource resource, String resourcePath,
            STGroupBundleDir stGroup, Theme theme) {
        if (theBundle.isPresent()) {
            String mediaTypedResourcePath = (resourcePath + "/" + theme).replace("/*", "");
            importTemplates(resource, mediaTypedResourcePath, stGroup, theBundle.get());
            importTemplates(resource, mediaTypedResourcePath + "/head", stGroup, theBundle.get());
            importTemplates(resource, mediaTypedResourcePath + "/navigation", stGroup, theBundle.get());
            importTemplates(resource, resourcePath + "/common", stGroup, theBundle.get());
            importTemplates(resource, resourcePath + "/common/head", stGroup, theBundle.get());
            importTemplates(resource, resourcePath + "/common/navigation", stGroup, theBundle.get());
        }
    }

    private void importTemplates(Resource resource, String resourcePath, STGroupBundleDir stGroup,
            Bundle theBundle) {
        if (resourcePathExists(resourcePath, theBundle)) {
            importedGroupBundleDir = new STGroupBundleDir(theBundle, resource, resourcePath,
                    htmlConverter.getTemplateProvider());
            stGroup.importTemplates(importedGroupBundleDir);
            log.debug("importing templates from {}: '{}'", theBundle.getSymbolicName(), resourcePath);
        }
    }

    private static boolean resourcePathExists(String resourcePath, Bundle theBundle) {
        return theBundle.getResource(resourcePath) != null;
    }

    private Optional<Bundle> findBundle(BundleContext bundleContext, String bundleName) {
        Bundle[] bundles = bundleContext.getBundles();
        Optional<Bundle> thisBundle = Arrays.stream(bundles)
                .filter(b -> b.getSymbolicName().equals(bundleName))
                .findFirst();
        return thisBundle;
    }

    private void checkForInspection(Resource resource, ST index) {
        if (resource == null || resource.getRequest() == null || resource.getRequest().getAttributes() == null) {
            return;
        }
        Object inspect = resource.getRequest().getAttributes().get(SkysailServerResource.INSPECT_PARAM_NAME);
        if (resource.getHostRef().getHostDomain().contains("localhost") && inspect != null) {
            index.inspect();
        }
    }

    private boolean bundleProvidesTemplates(Bundle appBundle) {
        return appBundle.getResource(TEMPLATES_DIR) != null;
    }

    private Bundle determineBundleToUse() {
        if (bundleProvidesTemplates(appBundle)) {
            return appBundle;
        }
        return findBundle(appBundle.getBundleContext(), SKYSAIL_SERVER_CONVERTER).get(); // NOSONAR
    }

    private List<String> matchingExtension(String identifierFragment) {
        return uiPolymerExtensions.stream()
                .filter(e -> e.contains(identifierFragment))
                .map(l -> {
                    String[] split = l.split("/");
                    return split[split.length - 1].replace(".html", "");
                })
                .collect(Collectors.toList());
    }

}
