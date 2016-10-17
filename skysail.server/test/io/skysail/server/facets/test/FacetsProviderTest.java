package io.skysail.server.facets.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.domain.jvm.facets.YearFacet;
import io.skysail.server.facets.FacetsProvider;

public class FacetsProviderTest {

    private FacetsProvider facetsProvider;
    private Map<String, String> config;

    @Before
    public void before() {
        facetsProvider = new FacetsProvider();
        config = new HashMap<>();
    }

    @Test
    public void yearfacet_with_provided_range() {
        config.put("io.skysail.some.attribute.TYPE", "YEAR");
        config.put("io.skysail.some.attribute.START", "2012");
        config.put("io.skysail.some.attribute.END", "2016");

        facetsProvider.activate(config);

        FieldFacet ff = facetsProvider.getFacetFor("io.skysail.some.attribute");
        assertThat(ff,is(not(nullValue())));
        assertThat(((YearFacet)ff).getStart(),is(2012));
        assertThat(((YearFacet)ff).getEnd(),is(2016));
    }

    @Test
    public void yearfacet_without_start() {
        config.put("io.skysail.some.attribute.TYPE", "YEAR");
        config.put("io.skysail.some.attribute.END", "2099");

        facetsProvider.activate(config);

        FieldFacet ff = facetsProvider.getFacetFor("io.skysail.some.attribute");
        assertThat(ff,is(not(nullValue())));
        assertThat(((YearFacet)ff).getStart(),is(new Date().getYear()+1900));
        assertThat(((YearFacet)ff).getEnd(),is(2099));
    }

    @Test
    public void yearfacet_without_end() {
        config.put("io.skysail.some.attribute.TYPE", "YEAR");
        config.put("io.skysail.some.attribute.START", "1980");

        facetsProvider.activate(config);

        FieldFacet ff = facetsProvider.getFacetFor("io.skysail.some.attribute");
        assertThat(ff,is(not(nullValue())));
        assertThat(((YearFacet)ff).getStart(),is(1980));
        assertThat(((YearFacet)ff).getEnd(),is(new Date().getYear()+1900));
    }

}
