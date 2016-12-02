package io.skysail.server.converter.impl;

import java.util.Arrays;
import java.util.Collections;
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
import io.skysail.server.Constants;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.caches.Caches;
import io.skysail.server.converter.HtmlConverter;
import io.skysail.server.converter.wrapper.STUserWrapper;
import io.skysail.server.filter.FilterParser;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.model.ResourceModel;
import io.skysail.server.rendering.RenderingMode;
import io.skysail.server.rendering.Theme;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.restlet.response.messages.Message;
import io.skysail.server.services.InstallationProvider;
import io.skysail.server.services.ThemeDefinition;
import io.skysail.server.utils.CookiesUtils;
import io.skysail.server.utils.RequestUtils;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Leverages StringTemplate engine to create html for resource representation.
 *
 */
@Slf4j
public class StringTemplateRenderer {

	private static final String SKYSAIL_SERVER_CONVERTER = "skysail.server.converter";
	private static final String TEMPLATES_DIR = "/templates";
	private static final String INDEX_FOR_MOBILES = "indexMobile";

	@Setter
	private Set<MenuItemProvider> menuProviders;

	@Setter
	private FilterParser filterParser;

	@Setter
	private InstallationProvider installationProvider;

	private STGroupBundleDir importedGroupBundleDir;
	private HtmlConverter htmlConverter;
	private Resource resource;
	private Theme theme;
	private RenderingMode mode;
	private Bundle appBundle;

	public StringTemplateRenderer(HtmlConverter htmlConverter, Resource resource) {
		this.htmlConverter = htmlConverter;
		this.resource = resource;
		this.appBundle = ((SkysailApplication) resource.getApplication()).getBundle();
		this.mode = CookiesUtils.getModeFromCookie(resource.getRequest()); // edit, debug, default
		log.info("created StringTemplateRenderer '{}'", toString());
	}

	public StringRepresentation createRepresenation(Object entity, Variant target, SkysailServerResource<?> resource) {

		theme = Theme.determineFrom(resource, target); // e.g. bootstrap, jquerymobile

		STGroupBundleDir.clearUsedTemplates();
		STGroup stGroup = createStringTemplateGroup(resource, theme);
		ST index = getStringTemplateIndex(resource, stGroup);

		addSubstitutions(createResourceModel(entity, target, resource), index);
		checkForInspection(resource, index);

		return createRepresentation(index, stGroup);
	}

	public List<Notification> getNotifications() {
		List<Notification> notifications = htmlConverter.getNotifications();
		String messageIds = resource.getOriginalRef().getQueryAsForm().getFirstValue("msgIds");
		if (messageIds != null) {
			List<Message> messages = Arrays.stream(messageIds.split("|")).map(id -> getMessageFromCache(id))
					.filter(msg -> msg != null).collect(Collectors.toList());
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

	public List<ThemeDefinition> getThemes() {
		List<ThemeDefinition> themeDefs = htmlConverter.getThemeProviders().stream().map(p -> p.getTheme())
				.collect(Collectors.toList());
		themeDefs.stream().forEach(td -> setIsSelected(td)); // NOSONAR
		return themeDefs;
	}

	public boolean isDebug() {
		return mode.equals(RenderingMode.DEBUG);
	}

	public boolean isEdit() {
		return mode.equals(RenderingMode.EDIT);
	}

	public List<String> getPeitybars() {
		return htmlConverter.getPeitybars();
	}

	private STGroup createStringTemplateGroup(Resource resource, Theme theme) {
		STGroupBundleDir stGroup = new STGroupBundleDir(determineBundleToUse(), resource, TEMPLATES_DIR, htmlConverter.getTemplateProvider());
		importFrom(resource, theme, stGroup, SKYSAIL_SERVER_CONVERTER);
		importFrom(resource, theme, stGroup, System.getProperty(Constants.PRODUCT_BUNDLE_IDENTIFIER));
		log.info("created StringTemplateGroup '{}'", stGroup.toString());
		return stGroup;
	}

	private ST getStringTemplateIndex(Resource resource, STGroup stGroup) {
		if (resource.getContext() != null
				&& resource.getContext().getAttributes().containsKey(ResourceContextId.RENDERER_HINT.name())) {
			String root = (String) resource.getContext().getAttributes().get(ResourceContextId.RENDERER_HINT.name());
			resource.getContext().getAttributes().remove(ResourceContextId.RENDERER_HINT.name());
			return stGroup.getInstanceOf(root);
		}

		if (RequestUtils.isMobile(resource.getRequest())) {
			return stGroup.getInstanceOf(INDEX_FOR_MOBILES);
		}
		Optional<String> indexPageName = getIndexPageNameFromCookie(resource);
		if (indexPageName.isPresent()) {
			return stGroup.getInstanceOf(indexPageName.get());
		}
		return stGroup.getInstanceOf("index");
		
	}

	private Optional<String> getIndexPageNameFromCookie(Resource resource) {
		return CookiesUtils.getMainPageFromCookie(resource.getRequest());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ResourceModel<SkysailServerResource<?>, ?> createResourceModel(Object entity, Variant target,
			SkysailServerResource<?> resource) {

		ResourceModel<SkysailServerResource<?>, ?> resourceModel = new ResourceModel(resource,
				(SkysailResponse<?>) entity, target, theme);
		resourceModel.setMenuItemProviders(menuProviders);
		resourceModel.setFilterParser(filterParser);
		resourceModel.setInstallationProvider(installationProvider);

		resourceModel.process();
		return resourceModel;
	}

	private StringRepresentation createRepresentation(ST index, STGroup stGroup) {
		String stringTemplateRenderedHtml = index.render();

		// index.getEvents()

		if (importedGroupBundleDir != null && stGroup instanceof STGroupBundleDir) {
			((STGroupBundleDir) stGroup).addUsedTemplates(importedGroupBundleDir.getUsedTemplates());
		}
		String templatesHtml = isDebug() ? getTemplatesHtml(stGroup) : "";
		StringRepresentation rep = new StringRepresentation(
				stringTemplateRenderedHtml.replace("%%templates%%", templatesHtml));

		rep.setMediaType(MediaType.TEXT_HTML);
		return rep;
	}

	/**
	 * As the templates used for creating the output cannot be added to the
	 * output itself during creation time, they are added in an additional step
	 * here
	 *
	 * @param stGroup
	 */
	private String getTemplatesHtml(STGroup stGroup) {
		StringBuilder sb = new StringBuilder();
		sb.append(stGroup.toString().replace("\n", "<br>\n")).append("\n<hr>");
		if (stGroup instanceof STGroupBundleDir) {
			String templates = ((STGroupBundleDir) stGroup).getUsedTemplates().stream()
					.map(template -> "<li>" + template + "</li>").collect(Collectors.joining("\n"));
			sb.append("<ul>").append(templates).append("</ul>");
		}
		return sb.toString();
	}

	private void addSubstitutions(ResourceModel<SkysailServerResource<?>, ?> resourceModel, @NonNull ST decl) {

		SkysailServerResource<?> resource = resourceModel.getResource();

		Optional<String> installationFromCookie = CookiesUtils.getInstallationFromCookie(resource.getRequest());

		decl.add("user", new STUserWrapper(htmlConverter.getUserManagementProvider(), resourceModel.getResource(),
				installationFromCookie.get()));
		decl.add("converter", this);

		Map<String, Translation> messages = resource.getMessages(resourceModel.getFields());
		messages.put("productName", new Translation(getProductName(), null, Collections.emptySet()));
		messages.put("productVersion", new Translation("1.2.3", null, Collections.emptySet()));

		decl.add("messages", messages);
		decl.add("model", resourceModel);
	}

	private Message getMessageFromCache(String id) {
		// CacheStats messageCacheStats = Caches.getMessageCacheStats();
		// System.out.println(messageCacheStats);
		return Caches.getMessageCache().getIfPresent(Long.valueOf(id));
	}

	private void importTemplates(Optional<Bundle> theBundle, Resource resource, String resourcePath,
			STGroupBundleDir stGroup, Theme theme) {
		if (theBundle.isPresent()) {
			String mediaTypedResourcePath = (resourcePath + "/" + theme).replace("/*", "");
			importTemplates(resource, mediaTypedResourcePath, stGroup, theBundle);
			importTemplates(resource, mediaTypedResourcePath + "/head", stGroup, theBundle);
			importTemplates(resource, mediaTypedResourcePath + "/navigation", stGroup, theBundle);
			importTemplates(resource, resourcePath + "/common", stGroup, theBundle);
			importTemplates(resource, resourcePath + "/common/head", stGroup, theBundle);
			importTemplates(resource, resourcePath + "/common/navigation", stGroup, theBundle);
		}
	}

	private void importTemplates(Resource resource, String resourcePath, STGroupBundleDir stGroup,
			Optional<Bundle> theBundle) {
		if (resourcePathExists(resourcePath, theBundle)) {
			importedGroupBundleDir = new STGroupBundleDir(theBundle.get(), resource, resourcePath,
					htmlConverter.getTemplateProvider());
			stGroup.importTemplates(importedGroupBundleDir);
			log.debug("importing templates from {}: '{}'", theBundle.get().getSymbolicName(), resourcePath);
		}
	}

	private static boolean resourcePathExists(String resourcePath, Optional<Bundle> theBundle) {
		return theBundle.get().getResource(resourcePath) != null;
	}

	private synchronized String getProductName() {
		if (htmlConverter == null) {
			return "Skysail";
		}
		return htmlConverter.getProductName();
	}

	private Optional<Bundle> findBundle(BundleContext bundleContext, String bundleName) {
		Bundle[] bundles = bundleContext.getBundles();
		Optional<Bundle> thisBundle = Arrays.stream(bundles).filter(b -> b.getSymbolicName().equals(bundleName))
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

	private void setIsSelected(ThemeDefinition themeDef) {
		themeDef.setSelected(false);
		if (themeDef.getName().equalsIgnoreCase(theme.getVariant().name())) {
			themeDef.setSelected(true);
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

	private void importFrom(Resource resource, Theme theme, STGroupBundleDir stGroup, String symbolicName) {
		Optional<Bundle> theBundle = findBundle(appBundle.getBundleContext(), symbolicName);
		importTemplates(theBundle, resource, TEMPLATES_DIR, stGroup, theme);
	}

}
