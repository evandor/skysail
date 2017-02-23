//package io.skysail.server.app.ref.fields.test;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertThat;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.restlet.Response;
//
//import io.skysail.server.app.ref.fields.FieldsDemoApplication;
//import io.skysail.server.app.ref.fields.domain.TextEntity;
//import io.skysail.server.app.ref.fields.domain.TextEntitysResource;
//import io.skysail.server.menus.MenuItem;
//import io.skysail.server.menus.MenuItemProvider;
//import io.skysail.server.testsupport.ResourceTestBase2;
//
//public class TextEntitysResourceTest extends ResourceTestBase2 {
//
//    @Mock
//    private MenuItemProvider menuItemProvider;
//
//    @Before
//    public void setup() throws Exception {
//        super.setUp(new FieldsDemoApplication());
//
//        resource = new TextEntitysResource();
//        resource.setRequest(request);
//
//        // inject(MenusApplication.class, "menuItemProvider", menuItemProvider);
//        //((FieldsDemoApplication) application).addMenuProvider(menuItemProvider);
//
//        resource.init(context, request, new Response(request));
//    }
//
//    @SuppressWarnings("unchecked")
//    @Test
//    public void menuItemList_is_empty_if_menuProvider_does_not_contain_menuItems() {
//        List<TextEntity> menuItems = (List<TextEntity>) resource.getEntity();
//        assertThat(menuItems.size(), is(0));
//    }
//
//    @SuppressWarnings("unchecked")
//    @Test
//    public void testname() {
//        Mockito.when(menuItemProvider.getMenuEntries()).thenReturn(Arrays.asList(new MenuItem("name","link")));
//        List<TextEntity> items = (List<TextEntity>) resource.getEntity();
//        assertThat(items.size(), is(1));
//    }
//
//}