package io.skysail.server.app.starmoney.transactions;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import io.skysail.api.links.Link;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.starmoney.Import2MemoryProcessor;
import io.skysail.server.app.starmoney.StarMoneyApplication;
import io.skysail.server.app.starmoney.StarMoneyRepository;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.domain.jvm.SkysailEntityModel;
import io.skysail.server.domain.jvm.SkysailFieldModel;
import io.skysail.server.ext.starmoney.domain.Account;
import io.skysail.server.ext.starmoney.domain.Transaction;
import io.skysail.server.facets.FacetsProvider;
import io.skysail.server.restlet.resources.ListServerResource;

public class AccountsTransactionsResource extends ListServerResource<Transaction> {

    private StarMoneyApplication app;
    private StarMoneyRepository repo;

    public AccountsTransactionsResource() {
        // super(TodoListsTodoResource.class);
        addToContext(ResourceContextId.LINK_GLYPH, "list");
        addToContext(ResourceContextId.LINK_TITLE, "list transactions for this account");
    }

    @Override
    protected void doInit() {
        app = (StarMoneyApplication) getApplication();
        repo = (StarMoneyRepository) app.getRepository(Account.class);
    }

    @Override
    public List<Transaction> getEntity() {
        Account account = Import2MemoryProcessor.getAccounts().stream().filter(a -> {
            //String theId = "#"+getAttribute("id");
            String theId = getAttribute("id");
            return a.getId().equals(theId);
        }).findFirst().orElse(new Account());
//        Filter filter = new Filter(getRequest(),getFacetsFor(Transaction.class));
//        Pagination pagination = new Pagination(getRequest(), getResponse());
//        Sorting sorting = new Sorting(getRequest());
//        List<Transaction> transactions = repo.find(
//                Transaction.class, "#" + getAttribute("id") + " in IN(transactions)", filter, sorting, pagination);
        List<Transaction> transactions = account.getTransactions();
        handleFacets(transactions, getApplicationModel());
        return transactions;
    }

    private Map<String, FieldFacet> getFacetsFor(Class<Transaction> cls) {
        Map<String, FieldFacet> result = new HashMap<>();
        FacetsProvider facetsProvider = getApplication().getFacetsProvider();
        Optional<SkysailEntityModel> findFirst = getApplicationModel().getEntityValues()
                .stream()
                .filter(v -> v.getId().equals(cls.getName())) // NOSONAR
                .map(SkysailEntityModel.class::cast)
                .findFirst();

        if (findFirst.isPresent()) {
            Collection<SkysailFieldModel> fieldValues = findFirst.get().getFieldValues();
            for (SkysailFieldModel fieldModel : fieldValues) {
                String ident = Transaction.class.getName() + "." + fieldModel.getId();
                try {
                    Field declaredField = Transaction.class.getDeclaredField(fieldModel.getId());
                    FieldFacet facetFor = facetsProvider.getFacetFor(ident);
                    result.put(fieldModel.getId(), facetFor);
                } catch (Exception e) {

                }
            }
        }
        return result;
    }

    private void handleFacets(List<Transaction> transactions, ApplicationModel applicationModel) {
        FacetsProvider facetsProvider = getApplication().getFacetsProvider();
        Optional<SkysailEntityModel> findFirst = applicationModel.getEntityValues()
                .stream()
                .filter(v -> v.getId().equals(Transaction.class.getName())) // NOSONAR
                .map(SkysailEntityModel.class::cast)
                .findFirst();

        if (findFirst.isPresent()) {
            Collection<SkysailFieldModel> fieldValues = findFirst.get().getFieldValues();
            for (SkysailFieldModel fieldModel : fieldValues) {
                String ident = Transaction.class.getName() + "." + fieldModel.getId();
                try {
                    Field declaredField = Transaction.class.getDeclaredField(fieldModel.getId());
                    FieldFacet facetFor = facetsProvider.getFacetFor(ident);
                    if (facetFor != null) {
                        declaredField.setAccessible(true);
                        Map<String, AtomicInteger> buckets = facetFor.bucketsFrom(declaredField, transactions);
                        System.out.println(buckets);
                        this.facets.add(facetFor, buckets);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(AccountsTransactionsResource.class, AccountsTransactionsSaldoResource.class);
    }
}
