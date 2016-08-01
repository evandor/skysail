package io.skysail.server.app.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.menus.resources.MenusResource;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true)
public class MenusApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "menus";

    private List<MenuItemProvider> menuProviders = new ArrayList<>();

    public MenusApplication() {
        super(APP_NAME, new ApiVersion(1));
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
    }

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

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("", MenusResource.class));
        router.attach(new RouteBuilder("/", MenusResource.class));
        router.attach(new RouteBuilder("/menus", MenusResource.class));
    }

    public List<MenuItem> getMenuItems() {
        return menuProviders.stream()
                .map(mP -> mP.getMenuEntries())
                .flatMap(m -> m.stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath());
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
   }

}
