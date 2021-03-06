package io.skysail.server.app.starmoney.camel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.core.model.SkysailApplicationModel;
import io.skysail.server.app.starmoney.csv.CSVLexer;
import io.skysail.server.app.starmoney.csv.CSVParser;
import io.skysail.server.app.starmoney.repos.DbAccountRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.ext.starmoney.domain.Account;
import io.skysail.server.ext.starmoney.domain.Transaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImportProcessor implements Processor {

    private List<Account> accounts = new ArrayList<>();

    private DbAccountRepository repository;
    private SkysailApplicationModel applicationModel;

    public ImportProcessor(DbAccountRepository repository, SkysailApplicationModel applicationModel) {
        this.repository = repository;
        this.applicationModel = applicationModel;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("Processing file: " + exchange);
        Message in = exchange.getIn();
        String payload = in.getBody(String.class);
        in.setBody(payload);
        CSVParser parse = parse(payload);
        List<List<String>> data = parse.file().data;
        Map<String, Integer> mapping = createMappingFrom(data.get(0));

        for (int i = 1; i < data.size(); i++) {
            Transaction transaction = new Transaction(mapping,data.get(i));
            Account theAccount = checkAccount(repository, applicationModel, transaction.getKontonummer(), transaction.getBankleitzahl());
            Optional<Transaction> existing = theAccount.getTransactions().parallelStream().filter(t -> t.getStarMoneyId().equals(transaction.getStarMoneyId())).findFirst();
            if (existing.isPresent()) {
                Transaction existingTransaction = existing.get();
                if (!existingTransaction.toString().equals(transaction.toString())) {
                    log.info("upd. {}", existingTransaction);
                    log.info("with {}", transaction);
                    theAccount.getTransactions().remove(existingTransaction);
                }
            }
            theAccount.getTransactions().add(transaction);

           // repo.save(theAccount, skysailApplication.getApplicationModel());
//            Optional<Identifiable> existing = repo.findOne("starMoneyId", transaction.getStarMoneyId());
//            if (existing.isPresent()) {
//                transaction.setId(existing.get().getId());
//                //repo.update(transaction, skysailApplication.getApplicationModel());
//            } else {
                //repo.save(transaction, skysailApplication.getApplicationModel());
//            }
        }
        accounts.stream().forEach(a -> {
            repository.save(a, applicationModel);
        });
    }

    private Account checkAccount(DbAccountRepository repo, SkysailApplicationModel javaApplicationModel, String kontonummer, String bankleitzahl) {
        Optional<Account> accountFromCache = accounts.stream()
            .filter(a -> {
                return a.getBankleitzahl().equals(bankleitzahl) && a.getKontonummer().equals(kontonummer);
             })
            .findFirst();
        if (accountFromCache.isPresent()) {
            return accountFromCache.get();
        }
        List<?> accountsFromDb = repo.execute(Account.class, "SELECT FROM " + DbClassName.of(Account.class)
            + " WHERE kontonummer=" + kontonummer + " AND bankleitzahl=" + bankleitzahl);
        Account account = new Account(kontonummer, bankleitzahl);
        if (accountsFromDb.size() == 0) {
            OrientVertex save = repo.save(account, javaApplicationModel);
            account.setId(save.getId().toString());
            accounts.add(account);
            return account;
        } else {
            accounts.add((Account) accountsFromDb.get(0));
            return (Account) accountsFromDb.get(0);
        }
    }

    private Map<String, Integer> createMappingFrom(List<String> header) {
        Map<String, Integer> mapping = new HashMap<>();
        //findAndSet(header, "kontonummer",  "Kontonummer",  mapping);
        mapping.put("kontonummer", 0);
        findAndSet(header, "bankleitzahl", "Bankleitzahl", mapping);
        findAndSet(header, "betrag",       "Betrag",       mapping);
        findAndSet(header, "buchungstag",  "Buchungstag",  mapping);
        findAndSet(header, "kategorie",    "Kategorie",       mapping);
        findAndSet(header, "starMoneyId",  "Laufende Nummer", mapping);
        findAndSet(header, "saldo",        "Saldo",           mapping);
        return mapping;
    }

    private void findAndSet(List<String> header, String attributeName, String searchString, Map<String, Integer> mapping) {
        for (int i = 0; i < header.size(); i++) {
            if (header.get(i).contains(searchString)) {
                mapping.put(attributeName, i);
                break;
            }
        }
    }

    private CSVParser parse(String inputString) {
        CharStream input = new ANTLRInputStream(inputString);
        CSVLexer lexer = new CSVLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new CSVParser(tokens);
    }


}
