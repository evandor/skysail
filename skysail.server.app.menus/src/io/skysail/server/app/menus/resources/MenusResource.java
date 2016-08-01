package io.skysail.server.app.menus.resources;

import java.util.List;

import io.skysail.server.app.menus.MenusApplication;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.restlet.resources.ListServerResource;

public class MenusResource extends ListServerResource<MenuItem>{

    private MenusApplication app;

    public MenusResource() {
        this.app = (MenusApplication)getApplication();
    }
    @Override
    public List<?> getEntity() {
        return app.getMenuItems();
    }

}
