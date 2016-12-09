package io.skysail.server.app.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.restlet.resources.EntityServerResource;

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
	private SkysailRootApplication app;

	@Override
    protected void doInit() {
        id = getAttribute("id");
        app = (SkysailRootApplication) getApplication();
    }

	@Override
	public ResourceMetadata getEntity() {
		return new ResourceMetadata(id);
	}
	
	@Override
	public SkysailResponse<?> eraseEntity() {
		return null;
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
