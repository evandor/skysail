package io.skysail.server.queryfilter.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.annotations.Component;

import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.FilterParser;
import io.skysail.server.filter.FilterVisitor;
import io.skysail.server.queryfilter.nodes.AndNode;
import io.skysail.server.queryfilter.nodes.EqualityNode;
import io.skysail.server.queryfilter.nodes.GreaterNode;
import io.skysail.server.queryfilter.nodes.IsInNode;
import io.skysail.server.queryfilter.nodes.LessNode;
import io.skysail.server.queryfilter.nodes.NotNode;
import io.skysail.server.queryfilter.nodes.OrNode;
import io.skysail.server.queryfilter.nodes.PresentNode;
import io.skysail.server.queryfilter.nodes.SubstringNode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author org.osgi.framework.FrameworkUtil.ExprNode.Parser
 *
 */
@Component
@Slf4j
@ToString
public class Parser implements FilterParser {

    private String filterstring;
    private char[] filterChars;
    private int pos;

    @Override
    public ExprNode parse(String filterstring) {
        this.filterstring = filterstring;
        filterChars = filterstring.toCharArray();
        pos = 0;
        ExprNode filter;
        try {
            filter = parse_filter();
            sanityCheck();
            return filter;
        } catch (ArrayIndexOutOfBoundsException | InvalidSyntaxException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    /* e.g. (buchungstag;YYYY=2006)
     */
    @Override
    public Set<String> getSelected(String filterParamValue) {
        Set<String> result = new HashSet<>();
        ExprNode node = parse(filterParamValue);
        Object accept = node.accept(new FilterVisitor() {

            @Override
            public Object visit(ExprNode node) {
                return node.getSelected();
            }
        });
        return (Set<String>)accept;
    }

    private ExprNode parse_filter() throws InvalidSyntaxException {
        skipWhiteSpace();
        if (filterChars[pos] != '(') {
            throw new InvalidSyntaxException("Missing '(': " + filterstring.substring(pos), filterstring);
        }
        pos++;
        ExprNode filter = parse_filtercomp();
        skipWhiteSpace();
        if (filterChars[pos] != ')') {
            throw new InvalidSyntaxException("Missing ')': " + filterstring.substring(pos), filterstring);
        }
        pos++;
        skipWhiteSpace();
        return filter;
    }

    private ExprNode parse_filtercomp() throws InvalidSyntaxException {
        skipWhiteSpace();

        char c = filterChars[pos];

        switch (c) {
        case '&': {
            pos++;
            return parse_and();
        }
        case '|': {
            pos++;
            return parse_or();
        }
        case '!': {
            pos++;
            return parse_not();
        }
        }
        return parseItem();
    }

    private ExprNode parse_and() throws InvalidSyntaxException {
        int lookahead = pos;
        skipWhiteSpace();

        if (filterChars[pos] != '(') {
            pos = lookahead - 1;
            return parseItem();
        }

        List<ExprNode> operands = new ArrayList<>(10);

        while (filterChars[pos] == '(') {
            ExprNode child = parse_filter();
            operands.add(child);
        }

        return new AndNode(operands);
    }

    private ExprNode parse_or() throws InvalidSyntaxException {
        int lookahead = pos;
        skipWhiteSpace();

        if (filterChars[pos] != '(') {
            pos = lookahead - 1;
            return parseItem();
        }

        List<ExprNode> operands = new ArrayList<>(10);

        while (filterChars[pos] == '(') {
            ExprNode child = parse_filter();
            operands.add(child);
        }

        return new OrNode(operands);
    }

    private ExprNode parse_not() throws InvalidSyntaxException {
        int lookahead = pos;
        skipWhiteSpace();

        if (filterChars[pos] != '(') {
            pos = lookahead - 1;
            return parseItem();
        }

        ExprNode child = parse_filter();

        return new NotNode(child);
    }

    private ExprNode parseItem() throws InvalidSyntaxException {
        String attr = parse_attr();

        skipWhiteSpace();

        switch (filterChars[pos]) {
        case '~': {
            if (filterChars[pos + 1] == '=') {
                pos += 2;
                return null;// new ExprNode(Operation.APPROX, attr,
                            // parse_value());
            }
            break;
        }
        case '>': {
            if (filterChars[pos + 1] == '=') {
                pos += 2;
                throw new InvalidSyntaxException("Invalid operator: >= not implemented", filterstring);
            } else {
                pos += 1;
                return new GreaterNode(attr, (String) parse_substring());
            }
        }
        case '<': {
            if (filterChars[pos + 1] == '=') {
                pos += 2;
                throw new InvalidSyntaxException("Invalid operator: <= not implemented", filterstring);
            } else {
                pos += 1;
                return new LessNode(attr, (String) parse_substring());
            }
        }
        case '=': {
            if (filterChars[pos + 1] == '*') {
                int oldpos = pos;
                pos += 2;
                skipWhiteSpace();
                if (filterChars[pos] == ')') {
                    return new PresentNode(attr, null);
                }
                pos = oldpos;
            }

            pos++;
            Object string = parse_substring();

            if (string instanceof String) {
                return new EqualityNode(attr, (String) string);
            }
            if (string instanceof String[]) {
                String[] value = (String[]) string;
                if (value.length == 3) {
                    return new SubstringNode(attr, value[1]);
                } else if (value.length == 2) {
                    if (value[0] == null) {
                        return new SubstringNode(attr, value[1]);
                    } else if (value[1] == null) {
                        return new SubstringNode(attr, value[0]);
                    }
                }
            }
            return null;

        }
        case '\u00A7': { // paragraph or section symbol, "element of", "is in",
                         // not standard LDAP syntax! will replace this whole
                         // thing with a ANTLR-based grammar
            pos++;
            Object string = parse_substring();

            if (string instanceof String) {
                return new IsInNode(attr, (String) string);
            }
            return null;
        }
        }

        throw new InvalidSyntaxException("Invalid operator: " + filterstring.substring(pos), filterstring);
    }

    private String parse_attr() throws InvalidSyntaxException {
        skipWhiteSpace();

        int begin = pos;
        int end = pos;

        char c = filterChars[pos];

        while (c != '~' && c != '\u00A7' && c != '<' && c != '>' && c != '=' && c != '(' && c != ')') {
            pos++;

            if (!Character.isWhitespace(c)) {
                end = pos;
            }

            c = filterChars[pos];
        }

        int length = end - begin;

        if (length == 0) {
            throw new InvalidSyntaxException("Missing attr: " + filterstring.substring(pos), filterstring);
        }

        return new String(filterChars, begin, length);
    }

    private Object parse_substring() throws InvalidSyntaxException {
        StringBuffer sb = new StringBuffer(filterChars.length - pos);

        List<String> operands = new ArrayList<>(10);

        boolean isMethod = false;

        parseloop: while (true) {
            char c = filterChars[pos];

            switch (c) {
            case ')': {
                if (!isMethod) {
                    if (sb.length() > 0) {
                        operands.add(sb.toString());
                    }
                    break parseloop;
                } else {
                    isMethod = false;
                    pos++;
                    sb.append(")");
                    break;
                }
            }

            case '(': {
                isMethod = true;
                pos += 1;
                sb.append("(");
                break;
            }

            case '*': {
                if (sb.length() > 0) {
                    operands.add(sb.toString());
                }

                sb.setLength(0);

                operands.add(null);
                pos++;

                break;
            }

            case '\\': {
                pos++;
                c = filterChars[pos];
                /* fall through into default */
            }

            default: {
                sb.append(c);
                pos++;
                break;
            }
            }
        }

        int size = operands.size();

        if (size == 0) {
            return "";
        }

        if (size == 1) {
            Object single = operands.get(0);

            if (single != null) {
                return single;
            }
        }

        return operands.toArray(new String[size]);
    }

    private void skipWhiteSpace() {
        for (int length = filterChars.length; (pos < length) && Character.isWhitespace(filterChars[pos]);) {
            pos++;
        }
    }

    private void sanityCheck() throws InvalidSyntaxException {
        if (pos != filterChars.length) {
            throw new InvalidSyntaxException("Extraneous trailing characters: " + filterstring.substring(pos),
                    filterstring);
        }
    }

}
