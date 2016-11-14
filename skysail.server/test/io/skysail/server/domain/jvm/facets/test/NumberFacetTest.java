package io.skysail.server.domain.jvm.facets.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.data.Form;

import io.skysail.server.domain.jvm.facets.NumberFacet;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.restlet.resources.FacetBuckets;

public class NumberFacetTest {

    public class Dummy {

        public double date;

        public Dummy(Double date) {
            this.date = date;
        }
    }

    private NumberFacet numberFacet;
	private Field field;
    private Map<String, String> config;

    @Before
    public void setUp() throws Exception {
        field = Dummy.class.getDeclaredField("date");
        field.setAccessible(true);
        config = new HashMap<>();
        config.put("BORDERS", "-100,0,100");
        numberFacet = new NumberFacet("id", config);
    }

    @Test
    public void test_to_string() {
        assertThat(numberFacet.toString(),is("NumberFacet(thresholds=[-100.0, 0.0, 100.0])"));
    }

    @Test
    public void testBucketsFrom() {
        FacetBuckets buckets = numberFacet.bucketsFrom(field,
                Arrays.asList(
                        new Dummy(-1.0),
                        new Dummy(1.0),
                        new Dummy(3.0),
                        new Dummy(1000.0)
                ));
        assertThat(buckets.getBuckets().size(), is(4));
        assertThat(buckets.getBuckets().get("0").get(), is(0));
        assertThat(buckets.getBuckets().get("1").get(), is(1));
        assertThat(buckets.getBuckets().get("2").get(), is(2));
       // assertThat(buckets.get("3").get(), is(1));
    }

    @Test(expected = IllegalStateException.class)
    public void config_must_contain_Borders_config() {
        new NumberFacet("id", new HashMap<>());
    }

    @Test
    public void firstBorderIndex_yields_first_Border_expression() {
        String sqlExpr = numberFacet.sqlFilterExpression("0","");
        assertThat(sqlExpr,is("id<-100.0"));
    }

    @Test
    public void secondBorderIndex_yields_second_Border_expression() {
        String sqlExpr = numberFacet.sqlFilterExpression("1","");
        assertThat(sqlExpr,is("id>-100.0 AND id<0.0"));
    }

    @Test
    public void thirdBorderIndex_yields_third_Border_expression() {
        String sqlExpr = numberFacet.sqlFilterExpression("2","");
        assertThat(sqlExpr,is("id>0.0 AND id<100.0"));
    }

    @Test
    public void fourthBorderIndex_yields_fourth_Border_expression() {
        String sqlExpr = numberFacet.sqlFilterExpression("3","");
        assertThat(sqlExpr,is("id>100.0"));
    }

    @Test
    public void does_not_match_bigger_number_for_lessNode() {
        ExprNode node = Mockito.mock(ExprNode.class);
        assertThat(numberFacet.match(node, 1.0, "0"),is(false));
    }

    @Test
    public void does_match_smaller_number_for_lessNode() {
        ExprNode node = Mockito.mock(ExprNode.class);
        Mockito.when(node.evaluateValue(1.0)).thenReturn(true);
        assertThat(numberFacet.match(node, 1.0, "0"),is(true));
    }

    @Test
    public void does_not_match_non_comparable_for_lessNode() {
        ExprNode node = Mockito.mock(ExprNode.class);
        assertThat(numberFacet.match(node, "1.0", "0"),is(false));
    }

    @Test
    public void form_parameter_is_set_to_matching_bucket() {
        assertThat(numberFacet.addFormParameters(new Form(), "b", "", "0").getFirstValue("_f"),is("(b<-100.0)"));
        assertThat(numberFacet.addFormParameters(new Form(), "b", "", "1").getFirstValue("_f"),is("(&(b>-100.0)(b<0.0))"));
        assertThat(numberFacet.addFormParameters(new Form(), "b", "", "2").getFirstValue("_f"),is("(&(b>0.0)(b<100.0))"));
        assertThat(numberFacet.addFormParameters(new Form(), "b", "", "3").getFirstValue("_f"),is("(b>100.0)"));
    }

}
