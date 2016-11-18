package io.skysail.server.app.starmoney;

import java.util.Arrays;

import org.apache.camel.core.osgi.OsgiDefaultCamelContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;

import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.starmoney.camel.ImportCsvRoute;
import io.skysail.server.app.starmoney.transactions.AccountsTransactionsPivotResource;
import io.skysail.server.app.starmoney.transactions.AccountsTransactionsPivotResource2;
import io.skysail.server.app.starmoney.transactions.AccountsTransactionsResource;
import io.skysail.server.app.starmoney.transactions.AccountsTransactionsSaldoResource;
import io.skysail.server.app.starmoney.transactions.TransactionsResource;
import io.skysail.server.camel.CamelContextProvider;
import io.skysail.server.ext.starmoney.domain.Account;
import io.skysail.server.ext.starmoney.domain.Transaction;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Slf4j
public class StarMoneyApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "starmoney";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    private OsgiDefaultCamelContext camelContext;

    public StarMoneyApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(Account.class, Transaction.class));
        setDescription("StarMoney Reporting");
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    @Override
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) { // NOSONAR
        super.setRepositories(null);
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
    }

    @Deactivate
    public void deactivate() {
        try {
            if (camelContext != null) {
                camelContext.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        camelContext = null;
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    private void setCamelContextProvider (CamelContextProvider provider) {
        camelContext = (OsgiDefaultCamelContext) provider.getCamelContext();
        log.info("camel context was provided to {}", this.getClass().getName());
        try {
            camelContext.addRoutes(new ImportCsvRoute((StarMoneyRepository)getRepository(Account.class),getApplicationModel()));
            camelContext.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private synchronized void  unsetCamelContextProvider (CamelContextProvider provider) { // NOSONAR
        log.info("unsetting camel context in {}", this.getClass().getName());
        try {
            //camelContext.stop();
            ((OsgiDefaultCamelContext)provider.getCamelContext()).shutdown();
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
                // .authorizeRequests().startsWithMatcher("/mailgun").permitAll().and()
                // .authorizeRequests().equalsMatcher("/Bookmarks/").permitAll().and()
                // .authorizeRequests().startsWithMatcher("/unprotected").permitAll().and()
                .authorizeRequests().startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new io.skysail.server.restlet.RouteBuilder("", AccountsResource.class));

        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts",      AccountsResource.class));
        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts/{id}", AccountResource.class));
        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts/{id}/", PutAccountResource.class));

        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts/{id}/transactions", AccountsTransactionsResource.class));
//        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts/{id}/transactions/{tid}", AccountTransactionsResource.class));

        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts/{id}/saldo", AccountsTransactionsSaldoResource.class));
        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts/{id}/pivot", AccountsTransactionsPivotResource.class));
        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts/{id}/pivot2", AccountsTransactionsPivotResource2.class));

        router.attach(new io.skysail.server.restlet.RouteBuilder("/Transactions", TransactionsResource.class));
    }

    public Account getAccount(String id) {
        return Import2MemoryProcessor.getAccounts().stream().filter(a -> {
            return a.getId().equals(id);
        }).findFirst().orElse(new Account());
    }

}