package io.skysail.server.app.starmoney.transactions;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.skysail.api.links.Link;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.EntityModel;
import io.skysail.domain.core.FieldModel;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.starmoney.Account;
import io.skysail.server.app.starmoney.StarMoneyApplication;
import io.skysail.server.app.starmoney.StarMoneyRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.domain.jvm.JavaEntityModel;
import io.skysail.server.domain.jvm.JavaFieldModel;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> getEntity() {
		String sql = "select * from " + DbClassName.of(Transaction.class) + " where #" + getAttribute("id")
				+ " in IN(transactions)";
		List<Transaction> transactions = (List<Transaction>) repo.execute(Transaction.class, sql);
		handleFacets(transactions, getApplicationModel());
		return transactions;
	}

	private void handleFacets(List<Transaction> transactions, ApplicationModel applicationModel) {
		Optional<JavaEntityModel> findFirst = applicationModel.getEntityValues().stream().filter(v -> {
			return v.getId().equals(Transaction.class.getName());
		}).map(JavaEntityModel.class::cast).findFirst();

		System.out.println(findFirst);
		if (findFirst.isPresent()) {
			Collection<JavaFieldModel> fieldValues = findFirst.get().getFieldValues();
			System.out.println(fieldValues);
			
			fieldValues.stream()
				.filter(f -> f.getFacet() != null)
				.map(f -> f.getFacet())
				.forEach(facet -> {
					Map<Integer, AtomicInteger> buckets = facet.bucketsFrom(transactions);
					System.out.println(buckets);
				});
		}

	}

	@Override
	public List<Link> getLinks() {
		return super.getLinks(AccountsTransactionsResource.class, AccountsTransactionsSaldoResource.class);
	}
}
