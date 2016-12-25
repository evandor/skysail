package io.skysail.server.app.resources;

import java.util.Optional;

import org.restlet.resource.ServerResource;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.EntityModel;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.domain.jvm.SkysailApplicationModel;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.resources.SkysailServerResource;

/**
 * Default resource, attached to path "/".
 *
 */
public class EntityMetaResource extends EntityServerResource<ResourceMetadata> {

//    @Override
//    public List<Link> getLinks() {
//        SkysailRootApplication defaultApp = (SkysailRootApplication) getApplication();
//        Set<MenuItem> menuItems = defaultApp.getMenuItems();
//        return menuItems.stream().map(this::createLinkForApp)
//                .sorted((l1, l2) -> l1.getTitle().compareTo(l2.getTitle())).collect(Collectors.toList());
//    }

    private String id;
	private SkysailApplication app;

	@Override
    protected void doInit() {
        id = getAttribute("id");
        app = (SkysailApplication) getApplication();
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
				Class<? extends Identifiable> parameterizedType = getResourcesGenericType(resourceInstance);
				EntityModel<?> entity = applicationModel.getEntity(parameterizedType.getName());
				return new ResourceMetadata(id, entity);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
		return new ResourceMetadata(id);
	}
	
	@Override
	public SkysailResponse<?> eraseEntity() {
		return null;
	}
	
	 @SuppressWarnings("unchecked")
	    private static Class<? extends Identifiable> getResourcesGenericType(SkysailServerResource<?> resourceInstance) {
	        return (Class<? extends Identifiable>) resourceInstance.getParameterizedType();
	    }


//    @Override
//    public String redirectTo() {
//        return ((SkysailRootApplication) getApplication()).getRedirectTo(this);
//    }
//
//    @Override
//    public List<MenuItemDescriptor> getEntity() {
//        Set<MenuItem> mainMenuItems = ((SkysailRootApplication)getApplication()).getMainMenuItems(this,getRequest());
//        return mainMenuItems.stream()
//                .map(i -> new MenuItemDescriptor(i))
//                .sorted((m1,m2) -> m1.getName().compareTo(m2.getName()))
//                .collect(Collectors.toList());
//    }

}
