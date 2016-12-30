// Generated from /Users/carsten/git/skysail/skysail.server.app.starmoney/antlr/io/skysail/server/app/starmoney/csv/CSV.g4 by ANTLR 4.5.3
package io.skysail.server.app.starmoney.csv;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CSVLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SemiColon=1, LineBreak=2, SimpleValue=3, QuotedValue=4, EmptyValue=5;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"SemiColon", "LineBreak", "SimpleValue", "QuotedValue", "EmptyValue"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "';'", null, null, null, "'<NULL>'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "SemiColon", "LineBreak", "SimpleValue", "QuotedValue", "EmptyValue"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public CSVLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CSV.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\7-\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\3\2\3\2\3\3\5\3\21\n\3\3\3\3\3\5\3\25\n"+
		"\3\3\4\6\4\30\n\4\r\4\16\4\31\3\5\3\5\3\5\3\5\7\5 \n\5\f\5\16\5#\13\5"+
		"\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\2\2\7\3\3\5\4\7\5\t\6\13\7\3\2\4"+
		"\6\2\f\f\17\17$$==\3\2$$\61\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3"+
		"\2\2\2\2\13\3\2\2\2\3\r\3\2\2\2\5\24\3\2\2\2\7\27\3\2\2\2\t\33\3\2\2\2"+
		"\13&\3\2\2\2\r\16\7=\2\2\16\4\3\2\2\2\17\21\7\17\2\2\20\17\3\2\2\2\20"+
		"\21\3\2\2\2\21\22\3\2\2\2\22\25\7\f\2\2\23\25\7\17\2\2\24\20\3\2\2\2\24"+
		"\23\3\2\2\2\25\6\3\2\2\2\26\30\n\2\2\2\27\26\3\2\2\2\30\31\3\2\2\2\31"+
		"\27\3\2\2\2\31\32\3\2\2\2\32\b\3\2\2\2\33!\7$\2\2\34\35\7$\2\2\35 \7$"+
		"\2\2\36 \n\3\2\2\37\34\3\2\2\2\37\36\3\2\2\2 #\3\2\2\2!\37\3\2\2\2!\""+
		"\3\2\2\2\"$\3\2\2\2#!\3\2\2\2$%\7$\2\2%\n\3\2\2\2&\'\7>\2\2\'(\7P\2\2"+
		"()\7W\2\2)*\7N\2\2*+\7N\2\2+,\7@\2\2,\f\3\2\2\2\b\2\20\24\31\37!\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}