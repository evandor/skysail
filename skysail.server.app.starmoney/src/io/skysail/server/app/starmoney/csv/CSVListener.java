// Generated from /Users/carsten/git/skysail/skysail.server.app.starmoney/antlr/io/skysail/server/app/starmoney/csv/CSV.g4 by ANTLR 4.5.3
package io.skysail.server.app.starmoney.csv;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CSVParser}.
 */
public interface CSVListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CSVParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(CSVParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSVParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(CSVParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSVParser#row}.
	 * @param ctx the parse tree
	 */
	void enterRow(CSVParser.RowContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSVParser#row}.
	 * @param ctx the parse tree
	 */
	void exitRow(CSVParser.RowContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSVParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(CSVParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSVParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(CSVParser.ValueContext ctx);
}