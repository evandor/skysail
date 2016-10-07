package io.skysail.server.app.starmoney.test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.junit.Ignore;
import org.junit.Test;

public class CsvReaderTest {

    @Test
    @Ignore
    public void testName() throws IOException {
        Reader in = new FileReader("test/test2.csv");

//        CSVFormat format = CSVFormat
//                .newFormat(';')
//                .withQuote('"')
//                .withFirstRecordAsHeader();
//        Iterable<CSVRecord> records =format.parse(in);
//
//        for (CSVRecord record : records) {
//            String lastName = record.get("Bankleitzahl");
//            String firstName = record.get("Betrag");
//        }

    }
}
