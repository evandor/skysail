package io.skysail.server.app.starmoney.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import io.skysail.server.app.starmoney.csv.CSVLexer;
import io.skysail.server.app.starmoney.csv.CSVParser;


public class CsvParserTest {

    @Test
    public void testSimplestPackage() {
        CSVParser parser = parse(
                "\"REVIEW_DATE\";\"AUTHOR\";\"ISBN\";\"DISCOUNTED_PRICE\"\n"+
                "\"1985/01/21\";\"Douglas Adams\";0345391802;5.95\n"+
                "\"1984/01/21\";\"Douglas2 Adams\";0555391802;7.95\n"
        );
        List<List<String>> data = parser.file().data;
        assertThat(data.size(),is(3));
        assertThat(data.get(0).get(0),is("REVIEW_DATE"));
    }

    private CSVParser parse(String inputString) {
        CharStream input = new ANTLRInputStream(inputString);
        CSVLexer lexer = new CSVLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new CSVParser(tokens);
    }
}
