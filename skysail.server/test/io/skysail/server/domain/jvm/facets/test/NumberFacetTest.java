package io.skysail.server.domain.jvm.facets.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import io.skysail.server.domain.jvm.facets.NumberFacet;

public class NumberFacetTest {

    public class Dummy {
        //@Facet(type = FacetType.NUMBER, value="0,100")
        private double date;

        public Dummy(Double date) {
            this.date = date;
        }
    }

    private NumberFacet numberFacet;
	private Field field;

    @Before
    public void setUp() throws Exception {
        field = Dummy.class.getDeclaredField("date");
        numberFacet = new NumberFacet("id", Collections.emptyMap());//field, "0,100");
    }

    @Test
    @Ignore
    public void testBucketsFrom() throws Exception {
        Map<Integer, AtomicInteger> buckets = numberFacet.bucketsFrom(field,
                Arrays.asList(
                        new Dummy(-1.0),
                        new Dummy(1.0),
                        new Dummy(3.0),
                        new Dummy(1000.0)
                ));
        assertThat(buckets.size(), is(3));
        assertThat(buckets.get(0).get(), is(1));
        assertThat(buckets.get(1).get(), is(2));
        assertThat(buckets.get(2).get(), is(1));
    }

}
