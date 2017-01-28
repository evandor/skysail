package io.skysail.server.queryfilter.nodes.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import io.skysail.domain.Entity;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.domain.jvm.facets.YearFacet;
import io.skysail.server.filter.EntityEvaluationFilterVisitor;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.FilterVisitor;
import io.skysail.server.filter.Operation;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;
import io.skysail.server.queryfilter.nodes.EqualityNode;
import lombok.Data;

public class EqualityNodeTest {

    @Data
    public class SomeEntity implements Entity {
        private String id, A, B;
    }

    @Before
    public void setUp() {
    }

    @Test
    public void defaultConstructor_creates_node_with_AND_operation_and_zero_children()  {
        EqualityNode equalityNode = new EqualityNode("A", "a");

        assertThat(equalityNode.getOperation(),is(Operation.EQUAL));
        assertThat(equalityNode.isLeaf(),is(true));
    }

    @Test
    public void listConstructor_creates_node_with_EQUAL_operation_and_assigns_the_children_parameter() {
        EqualityNode equalityNode = new EqualityNode("A", "a");

        assertThat(equalityNode.getOperation(),is(Operation.EQUAL));
    }

    @Test
    public void equalityNode_with_one_children_gets_rendered() {
        EqualityNode equalityNode = new EqualityNode("A", "a");

        assertThat(equalityNode.asLdapString(),is("(A=a)"));
    }

    @Test
    public void nodes_toString_method_provides_representation() {
        EqualityNode equalityNode = new EqualityNode("A", "a");

        assertThat(equalityNode.toString(),is("(A=a)"));
    }

    @Test
    public void reduce_removes_the_matching_child() {
        EqualityNode equalityNode = new EqualityNode("A", "a");

        assertThat(equalityNode.reduce("(A=a)", null, null).asLdapString(),is(""));
    }

    @Test
    public void reduce_does_not_remove_non_matching_child() {
        EqualityNode equalityNode = new EqualityNode("A", "a");

        assertThat(equalityNode.reduce("b", null, null).asLdapString(),is("(A=a)"));
    }


    @Test
    public void creates_a_simple_preparedStatement() {
        EqualityNode equalityNode = new EqualityNode("A", "a");

        Map<String, FieldFacet> facets = new HashMap<>();
        PreparedStatement createPreparedStatement = (PreparedStatement) equalityNode.accept(new SqlFilterVisitor(facets));

        assertThat(createPreparedStatement.getParams().size(),is(1));
        assertThat(createPreparedStatement.getParams().get("A"),is("a"));
        assertThat(createPreparedStatement.getSql(), is("A=:A"));
    }

    @Test
    public void creates_a_preparedStatement_with_year_facet() {
        EqualityNode equalityNode = new EqualityNode("A", "a");

        Map<String, FieldFacet> facets = new HashMap<>();
        Map<String, String> config = new HashMap<>();
        facets.put("A", new YearFacet("A", config));
        PreparedStatement createPreparedStatement = (PreparedStatement) equalityNode.accept(new SqlFilterVisitor(facets));

        assertThat(createPreparedStatement.getParams().size(),is(1));
        assertThat(createPreparedStatement.getParams().get("A"),is("a"));
        assertThat(createPreparedStatement.getSql(), is("A.format('YYYY')=:A"));
    }

    @Test
    public void evaluateEntity() {
        EqualityNode equalityNode = new EqualityNode("A", "a");
        Map<String, FieldFacet> facets = new HashMap<>();
        Map<String, String> config = new HashMap<>();
        facets.put("A", new YearFacet("A", config));

        SomeEntity someEntity = new SomeEntity();
        someEntity.setA("a");
        someEntity.setB("b");

        EntityEvaluationFilterVisitor entityEvaluationVisitor = new EntityEvaluationFilterVisitor(someEntity, facets);
        boolean evaluateEntity = equalityNode.evaluateEntity(entityEvaluationVisitor);

        assertThat(evaluateEntity,is(false));
    }


    @Test
    public void getSelected()  {
        EqualityNode equalityNode = new EqualityNode("A", "a");

        assertThat(equalityNode.getSelected(null,Collections.emptyMap()).size(),is(1));
        Iterator<String> iterator = equalityNode.getSelected(null,Collections.emptyMap()).iterator();
        assertThat(iterator.next(),is("a"));
    }

    @Test
    public void getKeys() {
        EqualityNode equalityNode = new EqualityNode("A", "a");

        assertThat(equalityNode.getKeys().size(),is(1));
        Iterator<String> iterator = equalityNode.getKeys().iterator();
        assertThat(iterator.next(),is("A"));
    }

    @Test
    public void accept() {
        EqualityNode equalityNode = new EqualityNode("A", "a");
        assertThat(equalityNode.accept(new FilterVisitor() {
            @Override
            public String visit(ExprNode node) {
                return ".";
            }

        }),is("."));
    }

}
