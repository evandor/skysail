package io.skysail.server.domain.jvm.facets.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

import io.skysail.server.domain.jvm.facets.NumberFacet;

public class NumberFacetTest {

    public class Dummy {

        private double date; // NOSONAR

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
    public void testBucketsFrom() {
        Map<String, AtomicInteger> buckets = numberFacet.bucketsFrom(field,
                Arrays.asList(
                        new Dummy(-1.0),
                        new Dummy(1.0),
                        new Dummy(3.0),
                        new Dummy(1000.0)
                ));
        assertThat(buckets.size(), is(4));
        assertThat(buckets.get("0").get(), is(0));
        assertThat(buckets.get("1").get(), is(1));
        assertThat(buckets.get("2").get(), is(2));
       // assertThat(buckets.get("3").get(), is(1));
    }

    @Test
    public void firstBorderIndex_yields_first_Border_expression() {
        String sqlExpr = numberFacet.sqlFilterExpression("0");
        assertThat(sqlExpr,is("id<-100"));
    }

    @Test
    public void secondBorderIndex_yields_second_Border_expression() {
        String sqlExpr = numberFacet.sqlFilterExpression("1");
        assertThat(sqlExpr,is("id>-100 AND id<0"));
    }

    @Test
    public void thirdBorderIndex_yields_third_Border_expression() {
        String sqlExpr = numberFacet.sqlFilterExpression("2");
        assertThat(sqlExpr,is("id>0 AND id<100"));
    }

    @Test
    public void fourthBorderIndex_yields_fourth_Border_expression() {
        String sqlExpr = numberFacet.sqlFilterExpression("3");
        assertThat(sqlExpr,is("id>100"));
    }

}
