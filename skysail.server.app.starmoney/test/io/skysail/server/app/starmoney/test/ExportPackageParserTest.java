package io.skysail.server.app.starmoney.test;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import io.skysail.server.app.starmoney.csv.CSVLexer;
import io.skysail.server.app.starmoney.csv.CSVParser;
import io.skysail.server.app.starmoney.csv.CSVParser.HdrContext;
import io.skysail.server.app.starmoney.csv.CSVParser.RowContext;


public class ExportPackageParserTest {

    @Test
    public void testSimplestPackage() {
        CSVParser parser = parse("\"REVIEW_DATE\",\"AUTHOR\",\"ISBN\",\"DISCOUNTED_PRICE\"\n\"1985/01/21\",\"Douglas Adams\",0345391802,5.95");
        HdrContext hdr = parser.hdr();
        RowContext row = parser.row();
        //assertTrue(hdr.children.size() == 2);
    }

//    @Test
//    public void testSimplePackageWithDot() {
//        ExportPackageParser parser = parse("io.skysail");
//        ExportPackageContext tree = parser.exportPackage();
//        assertTrue(parser.getNumberOfSyntaxErrors() == 0);
//        System.out.println(tree.toStringTree(parser)); // print LISP-style tree
//    }
//
//    @Test
//    public void testTwoSimplePackages() {
//        ExportPackageParser parser = parse("io,io.skysail");
//        ExportPackageContext tree = parser.exportPackage();
//        assertTrue(parser.getNumberOfSyntaxErrors() == 0);
//        System.out.println(tree.toStringTree(parser));
//    }
//
//    @Test
//    public void testPackageWithVersion() {
//        ExportPackageParser parser = parse("com.fasterxml;version=1.3");
//        ExportPackageContext tree = parser.exportPackage();
//        assertTrue(parser.getNumberOfSyntaxErrors() == 0);
//        System.out.println(tree.toStringTree(parser));
//    }
//
//    @Test
//    @Ignore
//    public void testPackageWithVersionInBrackets() {
//        ExportPackageParser parser = parse("com.fasterxml;version=\"2.9\"");
//        ExportPackageContext tree = parser.exportPackage();
//        assertTrue(parser.getNumberOfSyntaxErrors() == 0);
//        System.out.println(tree.toStringTree(parser));
//    }
//
//    @Test
//    public void testPackageWithSingleUsesWithoutBrackets() {
//        ExportPackageParser parser = parse("jackson.core;uses:=com.fasterxml.jackson.core.format");
//        ExportPackageContext tree = parser.exportPackage();
//        assertTrue(parser.getNumberOfSyntaxErrors() == 0);
//        System.out.println(tree.toStringTree(parser)); // print LISP-style tree
//    }
//
//    @Test
//    @Ignore
//    public void testPackageWithMultipleUses() {
//        ExportPackageParser parser = parse(
//                "jackson.core;uses:=\"com.fasterxml.jackson.core.format,com.fasterxml.jackson.core.io\"");
//        ExportPackageContext tree = parser.exportPackage();
//        assertTrue(parser.getNumberOfSyntaxErrors() == 0);
//        System.out.println(tree.toStringTree(parser)); // print LISP-style tree
//    }
//
    private CSVParser parse(String inputString) {
        CharStream input = new ANTLRInputStream(inputString);
        CSVLexer lexer = new CSVLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new CSVParser(tokens);
    }
}
