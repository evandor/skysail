package io.skysail.server.app.reference.singleentity.resources.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.restlet.Response;

import io.skysail.domain.core.Repositories;
import io.skysail.server.app.reference.singleentity.Account;
import io.skysail.server.app.reference.singleentity.SingleEntityApplication;
import io.skysail.server.app.reference.singleentity.SingleEntityRepository;
import io.skysail.server.app.reference.singleentity.resources.AccountsResource;
import io.skysail.server.db.OrientGraphDbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.testsupport.ResourceTestBase2;

public class AccountsResourceTest extends ResourceTestBase2 {

    @Mock
    private MenuItemProvider menuItemProvider;

    @Before
    public void setup() throws Exception {
        super.setUp(new SingleEntityApplication());

        resource = new AccountsResource();
        resource.setRequest(request);

        // inject(MenusApplication.class, "menuItemProvider", menuItemProvider);
        // ((SingleEntityApplication)
        // application).addMenuProvider(menuItemProvider);

        Repositories repos = new Repositories();
        SingleEntityRepository repo = new SingleEntityRepository();
        OrientGraphDbService dbService = new OrientGraphDbService();
        dbService.activate();
        repo.setDbService(dbService);
        repo.activate();
        repos.setRepository(repo);
        ((SingleEntityApplication) application).setRepositories(repos);

        resource.init(context, request, new Response(request));
    }

    @SuppressWarnings("unchecked")
    @Test
    @Ignore
    public void menuItemList_is_empty_if_menuProvider_does_not_contain_menuItems() {
        List<Account> accounts = (List<Account>) resource.getEntity();
        assertThat(accounts.size(), is(0));
    }

}
