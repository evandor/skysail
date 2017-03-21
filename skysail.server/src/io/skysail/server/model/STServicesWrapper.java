package io.skysail.server.model;

import java.util.Set;

import io.skysail.core.resources.SkysailServerResource;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.menus.MenuItem.Category;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.utils.MenuItemUtils;

public class STServicesWrapper {

    private Set<MenuItemProvider> menuProviders;
    private SkysailServerResource<?> resource;

    public STServicesWrapper(Set<MenuItemProvider> menuProviders,
            SkysailServerResource<?> resource) {
        this.menuProviders = menuProviders;
        this.resource = resource;
    }

    public Set<MenuItem> getMainMenuItems() {
        return MenuItemUtils.getMenuItems(menuProviders, resource, MenuItem.Category.APPLICATION_MAIN_MENU);
    }

    public Set<MenuItem> getFrontendMenuItems() {
        return MenuItemUtils.getMenuItems(menuProviders, resource, MenuItem.Category.FRONTENDS_MAIN_MENU);
    }

    public Set<MenuItem> getDesignerAppMenuItems() {
        return MenuItemUtils.getMenuItems(menuProviders, resource, MenuItem.Category.DESIGNER_APP_MENU);
    }

    public Set<MenuItem> getAdminMenuItems() {
        Set<MenuItem> menuItems = MenuItemUtils.getMenuItems(menuProviders, resource, MenuItem.Category.ADMIN_MENU);
        menuItems.addAll(MenuItemUtils.getMenuItems(menuProviders, resource, Category.ADMIN_MAIN_MENU_INTERACTIVITY));
        return menuItems;
    }

    public Set<MenuItem> getDesignerAppItems() {
        return MenuItemUtils.getMenuItems(menuProviders, resource, MenuItem.Category.DESIGNER_APP_MENU);
    }

}
