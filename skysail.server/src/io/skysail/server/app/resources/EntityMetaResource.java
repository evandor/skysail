package io.skysail.server.app.resources;

import java.util.Optional;

import org.restlet.resource.ServerResource;

import io.skysail.core.app.ApiVersion;
import io.skysail.core.app.SkysailApplication;
import io.skysail.core.model.SkysailApplicationModel;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.domain.core.EntityModel;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.restlet.resources.EntityServerResource;

/**
 * Default resource, attached to path "/".
 *
 */
public class EntityMetaResource extends EntityServerResource<ResourceMetadata> {

    // @Override
    // public List<Link> getLinks() {
    // SkysailRootApplication defaultApp = (SkysailRootApplication)
    // getApplication();
    // Set<MenuItem> menuItems = defaultApp.getMenuItems();
    // return menuItems.stream().map(this::createLinkForApp)
    // .sorted((l1, l2) ->
    // l1.getTitle().compareTo(l2.getTitle())).collect(Collectors.toList());
    // }

    private String id;
    private SkysailApplication app;

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = getApplication();
    }

    @Override
    public ResourceMetadata getEntity() {
        String resourcePath = getRequest().getOriginalRef().getPath();
        String associatedResourcePath = resourcePath.replaceAll(app.getName() + "/_meta/", "");
        Optional<RouteBuilder> routeBuilder = app.getSkysailRoutes().values().stream().filter(r -> {
            return r.getPathTemplate(new ApiVersion(1)).replace("{id}", id).equals(associatedResourcePath);
        }).findFirst();
        if (routeBuilder.isPresent()) {
            SkysailApplicationModel applicationModel = app.getApplicationModel();
            Class<? extends ServerResource> targetClass = routeBuilder.get().getTargetClass();

            try {
                SkysailServerResource<?> resourceInstance = (SkysailServerResource<?>) targetClass.newInstance();
                Class<? extends Entity> parameterizedType = getResourcesGenericType(resourceInstance);
                EntityModel<?> entity = applicationModel.getEntity(parameterizedType.getName());
                return new ResourceMetadata(id, entity);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return new ResourceMetadata(id);
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Entity> getResourcesGenericType(SkysailServerResource<?> resourceInstance) {
        return (Class<? extends Entity>) resourceInstance.getParameterizedType();
    }

    // @Override
    // public String redirectTo() {
    // return ((SkysailRootApplication) getApplication()).getRedirectTo(this);
    // }
    //
    // @Override
    // public List<MenuItemDescriptor> getEntity() {
    // Set<MenuItem> mainMenuItems =
    // ((SkysailRootApplication)getApplication()).getMainMenuItems(this,getRequest());
    // return mainMenuItems.stream()
    // .map(i -> new MenuItemDescriptor(i))
    // .sorted((m1,m2) -> m1.getName().compareTo(m2.getName()))
    // .collect(Collectors.toList());
    // }

}
