package io.skysail.server.app.resources;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Predicate;

import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.links.LinkRole;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.menus.MenuItemDescriptor;
import io.skysail.server.restlet.resources.ListServerResource;

/**
 * Default resource, attached to path "/".
 *
 */
public class DefaultResource extends ListServerResource<MenuItemDescriptor> {

    @Override
    public List<Link> getLinks() {
        SkysailRootApplication defaultApp = (SkysailRootApplication) getApplication();
        Set<MenuItem> menuItems = defaultApp.getMenuItems();
        return menuItems.stream().map(this::createLinkForApp)
                .sorted((l1, l2) -> l1.getTitle().compareTo(l2.getTitle())).collect(Collectors.toList());
    }

    private Link createLinkForApp(MenuItem mi) {
        Predicate<String[]> securedBy = null;
        return new Link.Builder(mi.getLink()).relation(LinkRelation.ITEM).title(mi.getName()).role(LinkRole.MENU_ITEM)
                .authenticationNeeded(true).needsRoles(securedBy).build();
    }

    @Override
    public String redirectTo() {
        return ((SkysailRootApplication) getApplication()).getRedirectTo(this);
    }

    @Override
    public List<MenuItemDescriptor> getEntity() {
        Set<MenuItem> mainMenuItems = ((SkysailRootApplication)getApplication()).getMainMenuItems(this,getRequest());
        return mainMenuItems.stream()
                .map(i -> new MenuItemDescriptor(i))
                .sorted((m1,m2) -> m1.getName().compareTo(m2.getName()))
                .collect(Collectors.toList());
    }

}
