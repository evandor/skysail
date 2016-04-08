package io.skysail.server.utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.restlet.security.Role;

import io.skysail.server.menus.MenuItem;
import io.skysail.server.menus.MenuItem.Category;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class MenuItemUtils {

    public static Set<MenuItem> getMenuItems(Set<MenuItemProvider> menuProviders, SkysailServerResource<?> resource, Category category) {
        return menuProviders.stream() //
                .map(provider -> provider.getMenuEntries()) //
                .filter(menuEntries -> (menuEntries != null)) //
                .flatMap(entries -> entries.stream()) //
                .collect(Collectors.toSet()) //
                .stream() //
                .filter(item -> (item.getCategory().equals(category))) //
                .sorted((a, b) -> {
                    return b.getName().compareTo(a.getName());
                }).filter(item -> isAuthorized(item, resource)).collect(Collectors.toSet());
    }

    private static boolean isAuthorized(MenuItem item, SkysailServerResource<?> resource) {
        boolean authenticated = resource.getApplication().isAuthenticated(resource.getRequest());
        List<Role> clientRoles = resource.getRequest().getClientInfo().getRoles();
        if (!item.getNeedsAuthentication()) {
            return true;
        }
        List<String> clienRoleNames = clientRoles.stream().map(cr -> cr.getName()).collect(Collectors.toList());
        if (item.getSecuredByRole() != null) {
            if (item.getSecuredByRole().apply(clienRoleNames.toArray(new String[clienRoleNames.size()]))) {
                return true;
            }
        } else {
            if (authenticated) {
                return true;
            }
        }
        return false;
    }
}
