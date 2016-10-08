package io.skysail.server.app.starmoney;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import io.skysail.domain.core.repos.Repository;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.starmoney.csv.CSVLexer;
import io.skysail.server.app.starmoney.csv.CSVParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImportProcessor implements Processor {

    private SkysailApplication skysailApplication;

    public ImportProcessor(SkysailApplication skysailApplication) {
        this.skysailApplication = skysailApplication;
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
        Repository repo = skysailApplication.getRepository(Transaction.class);
        for (int i = 1; i < data.size(); i++) {
            Transaction transaction = new Transaction(mapping,data.get(i));
            repo.save(transaction, skysailApplication.getApplicationModel());
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
