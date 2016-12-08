package io.skysail.server.app.resources;

import java.util.List;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.menus.MenuItemDescriptor;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.resources.ListServerResource;

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

    

	@Override
	public SkysailResponse<?> eraseEntity() {
		return null;
	}

	@Override
	public ResourceMetadata getEntity() {
		return new ResourceMetadata();
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
