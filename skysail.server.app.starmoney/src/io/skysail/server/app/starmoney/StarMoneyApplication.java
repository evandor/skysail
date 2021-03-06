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
import org.restlet.service.CorsService;

import io.skysail.core.app.ApiVersion;
import io.skysail.core.app.ApplicationConfiguration;
import io.skysail.core.app.ApplicationProvider;
import io.skysail.core.app.SkysailApplication;
import io.skysail.server.app.starmoney.accounts.AccountResource;
import io.skysail.server.app.starmoney.accounts.AccountsResource;
import io.skysail.server.app.starmoney.accounts.PutAccountResource;
import io.skysail.server.app.starmoney.camel.ImportCsvRoute;
import io.skysail.server.app.starmoney.config.StarMoneyApplicationConfiguration;
import io.skysail.server.app.starmoney.repos.AccountsInMemoryRepository;
import io.skysail.server.app.starmoney.repos.DbAccountRepository;
import io.skysail.server.app.starmoney.transactions.AccountTransactionResource;
import io.skysail.server.app.starmoney.transactions.AccountTransactionsPivotResource;
import io.skysail.server.app.starmoney.transactions.AccountTransactionsPivotResource2;
import io.skysail.server.app.starmoney.transactions.AccountTransactionsResource;
import io.skysail.server.app.starmoney.transactions.AccountTransactionsSaldoResource;
import io.skysail.server.app.starmoney.transactions.TransactionsResource;
import io.skysail.server.db.DbService;
import io.skysail.server.ext.starmoney.domain.Account;
import io.skysail.server.ext.starmoney.domain.Transaction;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Slf4j
public class StarMoneyApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "starmoney";

    private OsgiDefaultCamelContext camelContext;

    @Reference
    private DbService dbService;

    @Getter
    private DbAccountRepository dbRepo;

    @Getter
    private AccountsInMemoryRepository cvsRepo;

    public StarMoneyApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(Account.class, Transaction.class));
        setDescription("StarMoney Reporting");
    }

    @Activate
    public void activate(StarMoneyApplicationConfiguration config, ComponentContext componentContext) throws ConfigurationException {
    	activate(componentContext);
        this.host = config.host();

        camelContext = new OsgiDefaultCamelContext(componentContext.getBundleContext());
        cvsRepo = new AccountsInMemoryRepository();
        dbRepo = new DbAccountRepository(dbService);

        try {
            camelContext.addRoutes(new ImportCsvRoute(dbRepo, cvsRepo, config, getApplicationModel()));
            camelContext.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Deactivate
    public void deactivate() {
        try {
            if (camelContext != null) {
                camelContext.stop();
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        camelContext = null;
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
                .authorizeRequests().startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new io.skysail.server.restlet.RouteBuilder("", AccountsResource.class));

        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts",      AccountsResource.class));
        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts/{id}", AccountResource.class));
        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts/{id}/", PutAccountResource.class));

        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts/{id}/transactions", AccountTransactionsResource.class));
        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts/{id}/transactions/{starmoneyId}", AccountTransactionResource.class));

        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts/{id}/saldo", AccountTransactionsSaldoResource.class));
        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts/{id}/pivot", AccountTransactionsPivotResource.class));
        router.attach(new io.skysail.server.restlet.RouteBuilder("/Accounts/{id}/pivot2", AccountTransactionsPivotResource2.class));

        router.attach(new io.skysail.server.restlet.RouteBuilder("/Transactions", TransactionsResource.class));
    }

    public Account getAccount(String id) {
        return cvsRepo.findAll().stream().filter(a -> {
            return a.getId().equals(id);
        }).findFirst().orElse(new Account());
    }

}