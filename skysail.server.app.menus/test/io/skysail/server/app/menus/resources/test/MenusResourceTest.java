package io.skysail.server.app.menus.resources.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;

import io.skysail.server.app.menus.MenusApplication;
import io.skysail.server.app.menus.resources.MenusResource;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.testsupport.ResourceTestBase2;

@RunWith(MockitoJUnitRunner.class)
public class MenusResourceTest extends ResourceTestBase2 {

    @Mock
    private MenuItemProvider menuItemProvider;

    @Before
    public void setup() throws Exception {
        super.setUp(new MenusApplication());

        resource = new MenusResource();
        resource.setRequest(request);

        // inject(MenusApplication.class, "menuItemProvider", menuItemProvider);
        ((MenusApplication) application).addMenuProvider(menuItemProvider);

        resource.init(context, request, new Response(request));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void menuItemList_is_empty_if_menuProvider_does_not_contain_menuItems() {
        List<MenuItem> menuItems = (List<MenuItem>) resource.getEntity();
        assertThat(menuItems.size(), is(0));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testname() {
        Mockito.when(menuItemProvider.getMenuEntries()).thenReturn(Arrays.asList(new MenuItem("name","link")));
        List<MenuItem> items = (List<MenuItem>) resource.getEntity();
        assertThat(items.size(), is(1));
    }

}
