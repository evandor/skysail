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
import io.skysail.server.queryfilter.nodes.EqualityNode;
import io.skysail.server.queryfilter.nodes.OrNode;
import lombok.Data;

@RunWith(MockitoJUnitRunner.class)
public class OrNodeTest {

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
    public void defaultConstructor_creates_node_with_OR_operation_and_zero_children()  {
        OrNode orNode = new OrNode();

        assertThat(orNode.getOperation(),is(Operation.OR));
        assertThat(orNode.getChildList().size(),is(0));
        assertThat(orNode.isLeaf(),is(false));
    }

    @Test
    public void listConstructor_creates_node_with_AND_operation_and_assigns_the_children_parameter() {
        OrNode orNode = new OrNode(children);

        assertThat(orNode.getOperation(),is(Operation.OR));
        assertThat(orNode.getChildList().size(),is(2));
    }

    @Test
    public void orNode_with_one_children_gets_rendered() {
        children = new ArrayList<>();
        children.add(new EqualityNode("A", "a"));

        OrNode orNode = new OrNode(children);

        assertThat(orNode.render(),is("(|(A=a))"));
    }

    @Test
    public void nodes_toString_method_provides_representation() {
        OrNode orNode = new OrNode(children);

        assertThat(orNode.toString(),is("(|(A=a)(B=b))"));
    }


    @Test
    public void orNode_with_two_children_gets_rendered() {
        OrNode orNode = new OrNode(children);
        assertThat(orNode.render(),is("(|(A=a)(B=b))"));
    }

    @Test
    public void reduce_removes_the_matching_child() {
        children.add(new EqualityNode("C", "c"));
        OrNode orNode = new OrNode(children);

        assertThat(orNode.reduce("(B=b)",null,  null).render(),is("(|(A=a)(C=c))"));
    }

    @Test
    public void reduce_removes_the_matching_child_and_OR_operator_for_two_children() {
        OrNode orNode = new OrNode(children);

        assertThat(orNode.reduce("(A=a)", null, null).render(),is("(B=b)"));
    }

    @Test
    public void reduce_returns_NullNode_when_last_child_was_removed() {
        children = new ArrayList<>();
        children.add(new EqualityNode("B", "b"));

        OrNode orNode = new OrNode(children);

        assertThat(orNode.reduce("(B=b)", null, null).render(),is(""));
    }

    @Test
    public void creates_a_simple_preparedStatement() {
        OrNode orNode = new OrNode(children);

        Map<String, FieldFacet> facets = new HashMap<>();
        PreparedStatement createPreparedStatement = (PreparedStatement) orNode.accept(new SqlFilterVisitor(facets));

        assertThat(createPreparedStatement.getParams().size(),is(2));
        assertThat(createPreparedStatement.getParams().get("A"),is("a"));
        assertThat(createPreparedStatement.getParams().get("B"),is("b"));
        assertThat(createPreparedStatement.getSql(), is("A=:A OR B=:B"));
    }

    @Test
    public void creates_a_preparedStatement_with_year_facet() {
        OrNode orNode = new OrNode(children);

        Map<String, FieldFacet> facets = new HashMap<>();
        Map<String, String> config = new HashMap<>();
        facets.put("A", new YearFacet("A", config));
        PreparedStatement createPreparedStatement = (PreparedStatement) orNode.accept(new SqlFilterVisitor(facets));

        assertThat(createPreparedStatement.getParams().size(),is(2));
        assertThat(createPreparedStatement.getParams().get("A"),is("a"));
        assertThat(createPreparedStatement.getParams().get("B"),is("b"));
        assertThat(createPreparedStatement.getSql(), is("A.format('YYYY')=:A OR B=:B"));
    }

    @Test
    public void evaluateEntity() {
        OrNode orNode = new OrNode(children);
        Map<String, FieldFacet> facets = new HashMap<>();
        Map<String, String> config = new HashMap<>();
        facets.put("A", new YearFacet("A", config));

        SomeEntity someEntity = new SomeEntity();
        someEntity.setA("a");
        someEntity.setB("b");

        EntityEvaluationFilterVisitor entityEvaluationVisitor = new EntityEvaluationFilterVisitor(someEntity, facets);
        boolean evaluateEntity = orNode.evaluateEntity(entityEvaluationVisitor);

        assertThat(evaluateEntity,is(true)); // TODO
    }


    @Test
    public void getSelected() {
        OrNode orNode = new OrNode(children);

        assertThat(orNode.getSelected(null).size(),is(2));
        Iterator<String> iterator = orNode.getSelected(null).iterator();
        assertThat(iterator.next(),is("a"));
        assertThat(iterator.next(),is("b"));
    }

    @Test
    public void getKeys() {
        OrNode orNode = new OrNode(children);

        assertThat(orNode.getKeys().size(),is(2));
        Iterator<String> iterator = orNode.getKeys().iterator();
        assertThat(iterator.next(),is("A"));
        assertThat(iterator.next(),is("B"));
    }

    @Test
    public void accept() {
        OrNode orNode = new OrNode(children);
        assertThat(orNode.accept(new FilterVisitor() {
            @Override
            public String visit(ExprNode node) {
                return ".";
            }

        }),is("."));
    }

}
