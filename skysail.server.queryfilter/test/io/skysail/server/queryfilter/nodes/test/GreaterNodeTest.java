package io.skysail.server.queryfilter.nodes.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.domain.jvm.facets.YearFacet;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.FilterVisitor;
import io.skysail.server.filter.Operation;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;
import io.skysail.server.queryfilter.nodes.GreaterNode;
import lombok.Data;

public class GreaterNodeTest {

    @Data
    public class SomeEntity implements Identifiable {
        private String id, A, B;
    }

    @Test
    public void defaultConstructor_creates_node_with_AND_operation_and_zero_children()  {
        GreaterNode greaterNode = new GreaterNode("A", 0);

        assertThat(greaterNode.getOperation(),is(Operation.GREATER));
        assertThat(greaterNode.isLeaf(),is(true));
    }

    @Test
    public void listConstructor_creates_node_with_AND_operation_and_assigns_the_children_parameter() {
        GreaterNode greaterNode = new GreaterNode("A", 0);

        assertThat(greaterNode.getOperation(),is(Operation.GREATER));
    }

    @Test
    public void greaterNode_with_one_children_gets_rendered() {
        GreaterNode greaterNode = new GreaterNode("A", 0);

        assertThat(greaterNode.asLdapString(),is("(A>0)"));
    }

    @Test
    public void nodes_toString_method_provides_representation() {
        GreaterNode greaterNode = new GreaterNode("A", 0);

        assertThat(greaterNode.toString(),is("(A>0)"));
    }


    @Test
    public void reduce_removes_the_matching_child() {
        GreaterNode greaterNode = new GreaterNode("A", 0);

        assertThat(greaterNode.reduce("(A>0)", null, null).asLdapString(),is(""));
    }

    @Test
    public void reduce_does_not_remove_non_matching_child() {
        GreaterNode greaterNode = new GreaterNode("A", 0);

        assertThat(greaterNode.reduce("(B>0)", null, null).asLdapString(),is("(A>0)"));
    }

    @Test
    public void creates_a_simple_preparedStatement() {
        GreaterNode greaterNode = new GreaterNode("A", 0);

        Map<String, FieldFacet> facets = new HashMap<>();
        PreparedStatement createPreparedStatement = (PreparedStatement) greaterNode.accept(new SqlFilterVisitor(facets));

        assertThat(createPreparedStatement.getParams().get("A"),is("0"));
        assertThat(createPreparedStatement.getSql(), is("A>:A"));
    }

    @Test
    public void creates_a_preparedStatement_with_year_facet() {
        GreaterNode greaterNode = new GreaterNode("A", 0);

        Map<String, FieldFacet> facets = new HashMap<>();
        Map<String, String> config = new HashMap<>();
        facets.put("A", new YearFacet("A", config));
        PreparedStatement createPreparedStatement = (PreparedStatement) greaterNode.accept(new SqlFilterVisitor(facets));

        assertThat(createPreparedStatement.getParams().get("A"),is("0"));
        assertThat(createPreparedStatement.getSql(), is("A.format('YYYY')>:A"));
    }

//    @Test
//    public void evaluateEntity() {
//        GreaterNode lessNode = new GreaterNode("A", 0);
//        Map<String, FieldFacet> facets = new HashMap<>();
//        Map<String, String> config = new HashMap<>();
//        facets.put("A", new YearFacet("A", config));
//
//        SomeEntity someEntity = new SomeEntity();
//        someEntity.setA(0);
//        someEntity.setB("b");
//
//        EntityEvaluationFilterVisitor entityEvaluationVisitor = new EntityEvaluationFilterVisitor(someEntity, facets);
//        boolean evaluateEntity = lessNode.evaluateEntity(entityEvaluationVisitor);
//
//        assertThat(evaluateEntity,is(false));
//    }

//        EntityEvaluationFilterVisitor entityEvaluationVisitor = new EntityEvaluationFilterVisitor(someEntity, facets);
//        boolean evaluateEntity = greaterNode.evaluateEntity(entityEvaluationVisitor);
//
//        assertThat(evaluateEntity,is(false));
//    }


    @Test
    @Ignore
    public void getSelected() {
        GreaterNode greaterNode = new GreaterNode("A", 0);

        FieldFacet facet = new YearFacet("id", Collections.emptyMap());
        Iterator<String> iterator = greaterNode.getSelected(facet,Collections.emptyMap()).iterator();
        assertThat(iterator.next(),is("0"));
    }

    @Test
    public void getKeys() {
        GreaterNode greaterNode = new GreaterNode("A", 0);

        assertThat(greaterNode.getKeys().size(),is(1));
        Iterator<String> iterator = greaterNode.getKeys().iterator();
        assertThat(iterator.next(),is("A"));
    }

    @Test
    public void accept() {
        GreaterNode greaterNode = new GreaterNode("A", 0);
        assertThat(greaterNode.accept(new FilterVisitor() {
            @Override
            public String visit(ExprNode node) {
                return ".";
            }

        }),is("."));
    }
}
