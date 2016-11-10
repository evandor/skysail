package io.skysail.server.queryfilter.parser.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.Operation;
import io.skysail.server.queryfilter.nodes.IsInNode;
import io.skysail.server.queryfilter.nodes.LessNode;
import io.skysail.server.queryfilter.parser.Parser;

public class ParserTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void equality() throws Exception {
        ExprNode parsed = new Parser().parse("(status=ARCHIVED)");
        assertThat(parsed.getOperation(), is(equalTo(Operation.EQUAL)));
        assertThat(parsed.isLeaf(), is(true));
    }

    @Test
    public void isNotNull() throws Exception {
        ExprNode parsed = new Parser().parse("(status=*)");
        assertThat(parsed.getOperation(), is(equalTo(Operation.PRESENT)));
        assertThat(parsed.isLeaf(), is(true));
    }

    @Test
    public void isNull() throws Exception {
        ExprNode parsed = new Parser().parse("(!(status=*))");
        assertThat(parsed.getOperation(), is(equalTo(Operation.NOT)));
        assertThat(parsed.isLeaf(), is(false));
    }

    @Test
    public void like() throws Exception {
        ExprNode parsed = new Parser().parse("(name=*substring*)");
        assertThat(parsed.getOperation(), is(equalTo(Operation.SUBSTRING)));
        assertThat(parsed.isLeaf(), is(true));
    }

    @Test
    public void likeStart() throws Exception {
        ExprNode parsed = new Parser().parse("(name=*substring)");
        assertThat(parsed.getOperation(), is(equalTo(Operation.SUBSTRING)));
        assertThat(parsed.isLeaf(), is(true));
    }

    @Test
    public void likeEnd() throws Exception {
        ExprNode parsed = new Parser().parse("(name=substring*)");
        assertThat(parsed.getOperation(), is(equalTo(Operation.SUBSTRING)));
        assertThat(parsed.isLeaf(), is(true));
    }

    @Test
    public void not() throws Exception {
        ExprNode parsed = new Parser().parse("(!(status=ARCHIVED))");
        assertThat(parsed.getOperation(), is(equalTo(Operation.NOT)));
        assertThat(parsed.isLeaf(), is(false));
    }

    @Test
    public void and() throws Exception {
        ExprNode parsed = new Parser().parse("(&(status=ARCHIVED)(user=admin))");
        assertThat(parsed.getOperation(), is(equalTo(Operation.AND)));
        assertThat(parsed.isLeaf(), is(false));
    }

    @Test
    public void or() throws Exception {
        ExprNode parsed = new Parser().parse("(|(status=ARCHIVED)(user=admin))");
        assertThat(parsed.getOperation(), is(equalTo(Operation.OR)));
        assertThat(parsed.isLeaf(), is(false));
    }

    @Test
    public void in() throws Exception {
        ExprNode parsed = new Parser().parse("(#17:0 § out['parent'])");
        assertThat(parsed.getOperation(), is(equalTo(Operation.IN)));
        assertThat(parsed.isLeaf(), is(true));
        assertThat(parsed, is(instanceOf(IsInNode.class)));
        assertThat(((IsInNode) parsed).getValue(), is("out['parent']"));
    }

    @Test
    @Ignore // TODO
    public void less() throws Exception {
        ExprNode parsed = new Parser().parse("(due < date[])");
        assertThat(parsed.getOperation(), is(equalTo(Operation.LESS)));
        assertThat(parsed.isLeaf(), is(true));
        assertThat(parsed, is(instanceOf(LessNode.class)));
        assertThat(((LessNode) parsed).getValue(), is("date[]"));
    }

    @Test
    @Ignore // TODO
    public void complex_less() throws Exception {
        ExprNode parsed = new Parser().parse("(&(due < date())(!(status=ARCHIVED)))");
        assertThat(parsed.getOperation(), is(equalTo(Operation.AND)));
        assertThat(parsed.isLeaf(), is(false));
    }

    @Test
    @Ignore // TODO
    public void greater() {
        ExprNode parsed = new Parser().parse("(due > date())");
        assertThat(parsed.getOperation(), is(equalTo(Operation.GREATER)));
        assertThat(parsed.isLeaf(), is(true));
    }

    @Test
    public void getSelected_from_simple_expression() {
        Set<String> selected = new Parser().getSelected(null, "(buchungstag;YYYY=2009)");
        assertThat(selected.size(), is(1));

    }

    @Test
    public void testSelected() {
        Set<String> selected = new Parser().getSelected(null, "(|(buchungstag;YYYY=2009)(buchungstag;YYYY=2008))");
        assertThat(selected.size(), is(2));
    }

}
