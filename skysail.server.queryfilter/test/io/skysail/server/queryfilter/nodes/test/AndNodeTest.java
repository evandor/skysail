package io.skysail.server.queryfilter.nodes.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.domain.jvm.facets.YearFacet;
import io.skysail.server.filter.EntityEvaluationFilterVisitor;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.FilterVisitor;
import io.skysail.server.filter.Operation;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;
import io.skysail.server.queryfilter.nodes.AndNode;
import io.skysail.server.queryfilter.nodes.EqualityNode;
import lombok.Data;

@RunWith(MockitoJUnitRunner.class)
public class AndNodeTest {

    @Data
    public class SomeEntity implements Identifiable {
        private String id, A, B;
    }

    private List<ExprNode> children;

    @Before
    public void setUp() {
        children = new ArrayList<>();
        children.add(new EqualityNode("A", "a"));
        children.add(new EqualityNode("B", "b"));
    }

    @Test
    public void defaultConstructor_creates_node_with_AND_operation_and_zero_children()  {
        AndNode andNode = new AndNode();

        assertThat(andNode.getOperation(),is(Operation.AND));
        assertThat(andNode.getChildList().size(),is(0));
        assertThat(andNode.isLeaf(),is(false));
    }

    @Test
    public void listConstructor_creates_node_with_AND_operation_and_assigns_the_children_parameter() {
        AndNode andNode = new AndNode(children);

        assertThat(andNode.getOperation(),is(Operation.AND));
        assertThat(andNode.getChildList().size(),is(2));
    }

    @Test
    public void andNode_with_one_children_gets_rendered() {
        children = new ArrayList<>();
        children.add(new EqualityNode("A", "a"));

        AndNode andNode = new AndNode(children);

        assertThat(andNode.render(),is("(&(A=a))"));
    }

    @Test
    public void andNode_with_two_children_gets_rendered() {
        AndNode andNode = new AndNode(children);
        assertThat(andNode.render(),is("(&(A=a)(B=b))"));
    }

    @Test
    public void reduce_removes_the_matching_child() {
        children.add(new EqualityNode("C", "c"));
        AndNode andNode = new AndNode(children);

        assertThat(andNode.reduce("b", null).render(),is("(&(A=a)(C=c))"));
    }

    @Test
    public void reduce_removes_the_matching_child_and_OR_operator_for_two_children() {
        AndNode andNode = new AndNode(children);

        assertThat(andNode.reduce("a", null).render(),is("(B=b)"));
    }


    @Test
    public void reduce_returns_NullNode_when_last_child_was_removed() {
        children = new ArrayList<>();
        children.add(new EqualityNode("B", "b"));

        AndNode andNode = new AndNode(children);

        assertThat(andNode.reduce("b", null).render(),is(""));
    }

    @Test
    public void creates_a_simple_preparedStatement() {
        AndNode andNode = new AndNode(children);

        Map<String, FieldFacet> facets = new HashMap<>();
        PreparedStatement createPreparedStatement = (PreparedStatement) andNode.accept(new SqlFilterVisitor(facets));

        assertThat(createPreparedStatement.getParams().size(),is(2));
        assertThat(createPreparedStatement.getParams().get("A"),is("a"));
        assertThat(createPreparedStatement.getParams().get("B"),is("b"));
        assertThat(createPreparedStatement.getSql(), is("A=:A AND B=:B"));
    }

    @Test
    public void creates_a_preparedStatement_with_year_facet() {
        AndNode andNode = new AndNode(children);

        Map<String, FieldFacet> facets = new HashMap<>();
        Map<String, String> config = new HashMap<>();
        facets.put("A", new YearFacet("A", config));
        PreparedStatement createPreparedStatement = (PreparedStatement) andNode.accept(new SqlFilterVisitor(facets));

        assertThat(createPreparedStatement.getParams().size(),is(2));
        assertThat(createPreparedStatement.getParams().get("A"),is("a"));
        assertThat(createPreparedStatement.getParams().get("B"),is("b"));
        assertThat(createPreparedStatement.getSql(), is("A.format('YYYY')=:A AND B=:B"));
    }

    @Test
    public void evaluateEntity() {
        AndNode andNode = new AndNode(children);
        Map<String, FieldFacet> facets = new HashMap<>();
        Map<String, String> config = new HashMap<>();
        facets.put("A", new YearFacet("A", config));

        SomeEntity someEntity = new SomeEntity();
        someEntity.setA("a");
        someEntity.setB("b");

        EntityEvaluationFilterVisitor entityEvaluationVisitor = new EntityEvaluationFilterVisitor(someEntity, facets);
        boolean evaluateEntity = andNode.evaluateEntity(entityEvaluationVisitor);

        assertThat(evaluateEntity,is(false));
    }


    @Test
    public void getSelected() throws Exception {
        AndNode andNode = new AndNode(children);

        assertThat(andNode.getSelected().size(),is(2));
        Iterator<String> iterator = andNode.getSelected().iterator();
        assertThat(iterator.next(),is("a"));
        assertThat(iterator.next(),is("b"));
    }

    @Test
    public void getKeys() {
        AndNode andNode = new AndNode(children);

        assertThat(andNode.getKeys().size(),is(2));
        Iterator<String> iterator = andNode.getKeys().iterator();
        assertThat(iterator.next(),is("A"));
        assertThat(iterator.next(),is("B"));
    }

    @Test
    public void accept() {
        AndNode andNode = new AndNode(children);
        assertThat(andNode.accept(new FilterVisitor() {
            @Override
            public String visit(ExprNode node) {
                return ".";
            }

        }),is("."));
    }

}
