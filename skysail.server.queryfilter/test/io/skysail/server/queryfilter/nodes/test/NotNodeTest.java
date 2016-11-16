package io.skysail.server.queryfilter.nodes.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
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
import io.skysail.server.queryfilter.nodes.NotNode;
import lombok.Data;

@RunWith(MockitoJUnitRunner.class)
public class NotNodeTest {

    @Data
    public class SomeEntity implements Identifiable {
        private String id, A, B;
    }

    private EqualityNode child;

    @Before
    public void setUp() {
        child = new EqualityNode("A", "a");
    }

    @Test
    public void listConstructor_creates_node_with_NOT_operation_and_assigns_the_child_parameter() {
        NotNode notNode = new NotNode(child);

        assertThat(notNode.getOperation(),is(Operation.NOT));
        assertThat(notNode.getChildList().size(),is(1));
    }

    @Test
    public void notNode_with_one_child_gets_rendered() {
        NotNode notNode = new NotNode(child);

        assertThat(notNode.asLdapString(),is("(!(A=a))"));
    }

    @Test
    public void nodes_toString_method_provides_representation() {
        NotNode notNode = new NotNode(child);

        assertThat(notNode.toString(),is("(!(A=a))"));
    }

    @Test
    public void reduce_removes_the_matching_child() {
        NotNode notNode = new NotNode(child);

        assertThat(notNode.reduce("a", null, null).asLdapString(),is(""));
    }

    @Test
    public void creates_a_simple_preparedStatement() {
        NotNode notNode = new NotNode(child);

        Map<String, FieldFacet> facets = new HashMap<>();
        PreparedStatement createPreparedStatement = (PreparedStatement) notNode.accept(new SqlFilterVisitor(facets));

        assertThat(createPreparedStatement.getParams().size(),is(1));
        assertThat(createPreparedStatement.getParams().get("A"),is("a"));
        assertThat(createPreparedStatement.getSql(), is("NOT (A=:A)"));
    }

    @Test
    public void creates_a_preparedStatement_with_year_facet() {
        NotNode notNode = new NotNode(child);

        Map<String, FieldFacet> facets = new HashMap<>();
        Map<String, String> config = new HashMap<>();
        facets.put("A", new YearFacet("A", config));
        PreparedStatement createPreparedStatement = (PreparedStatement) notNode.accept(new SqlFilterVisitor(facets));

        assertThat(createPreparedStatement.getParams().size(),is(1));
        assertThat(createPreparedStatement.getParams().get("A"),is("a"));
        assertThat(createPreparedStatement.getSql(), is("NOT (A.format('YYYY')=:A)"));
    }

    @Test
    public void evaluateEntity() {
        NotNode notNode = new NotNode(child);
        Map<String, FieldFacet> facets = new HashMap<>();
        Map<String, String> config = new HashMap<>();
        facets.put("A", new YearFacet("A", config));

        SomeEntity someEntity = new SomeEntity();
        someEntity.setA("a");
        someEntity.setB("b");

        EntityEvaluationFilterVisitor entityEvaluationVisitor = new EntityEvaluationFilterVisitor(someEntity, facets);
        boolean evaluateEntity = notNode.evaluateEntity(entityEvaluationVisitor);

        assertThat(evaluateEntity,is(true)); // TODO
    }


    @Test
    @Ignore
    public void getSelected() {
        NotNode notNode = new NotNode(child);

        assertThat(notNode.getSelected(null,Collections.emptyMap()).size(),is(1));
        Iterator<String> iterator = notNode.getSelected(null,Collections.emptyMap()).iterator();
        assertThat(iterator.next(),is("a"));
    }

    @Test
    public void getKeys() {
        NotNode notNode = new NotNode(child);

        assertThat(notNode.getKeys().size(),is(1));
        Iterator<String> iterator = notNode.getKeys().iterator();
        assertThat(iterator.next(),is("A"));
    }

    @Test
    public void accept() {
        NotNode notNode = new NotNode(child);
        assertThat(notNode.accept(new FilterVisitor() {
            @Override
            public String visit(ExprNode node) {
                return ".";
            }

        }),is("."));
    }

}
