package io.skysail.server.queryfilter.parser.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.domain.jvm.facets.MatcherFacet;
import io.skysail.server.domain.jvm.facets.NumberFacet;
import io.skysail.server.domain.jvm.facets.YearFacet;
import io.skysail.server.queryfilter.parser.LdapParser;
import io.skysail.server.utils.params.FilterParamUtils;
import lombok.SneakyThrows;

/**
 * these tests use the real {@link LdapParser} implementation of the queryfilter bundle.
 *
 * see io.skysail.server.utils.params.test.FilterParamUtilsTest
 *
 */
public class FilterParamUtilsTest {

    private Reference originalRef;
    private Form form;
    private Request request = mock(Request.class);
    private Map<String, String> numberFacetConfig;
    private Map<String, String> yearFacetConfig;

    @Before
    public void setUp() {
        form = new Form();
        originalRef = Mockito.mock(org.restlet.data.Reference.class);
        when(request.getOriginalRef()).thenReturn(originalRef);

        when(originalRef.hasQuery()).thenReturn(true);
        when(originalRef.getQuery()).thenReturn(form.getQueryString());
        when(originalRef.getQueryAsForm()).thenReturn(form);
        when(originalRef.getHierarchicalPart()).thenReturn("");

        numberFacetConfig = new HashMap<>();
        numberFacetConfig.put("BORDERS", "0,100");

        yearFacetConfig = new HashMap<>();
        yearFacetConfig.put("START", "2014");
        yearFacetConfig.put("END", "2016");
    }

    // === NumberFacet Tests ====================================

    @Test
    @SneakyThrows
    public void setMatch_with_valid_bucket_ids_on_numberFacet_without_existing_filterParam_yields_proper_filterParams() {
        FieldFacet facet = new NumberFacet("a", numberFacetConfig);
        FilterParamUtils filterParamUtils = new FilterParamUtils("a", request,new LdapParser());
        assertThat(decode(filterParamUtils.setMatchFilter("0",facet)), is("?_f=(a<0.0)"));
        assertThat(decode(filterParamUtils.setMatchFilter("1",facet)), is("?_f=(&(a>0.0)(a<100.0))"));
        assertThat(decode(filterParamUtils.setMatchFilter("2",facet)), is("?_f=(a>100.0)"));
    }

    @Test
    @SneakyThrows
    public void setMatch_with_valid_bucket_ids_on_numberFacet_with_existing_filterParam_yields_proper_filterParams() {
        form.add(new Parameter("_f", "(a<0.0)"));
        FieldFacet facet = new NumberFacet("a", numberFacetConfig);
        FilterParamUtils filterParamUtils = new FilterParamUtils("a", request,new LdapParser());
        assertThat(decode(filterParamUtils.setMatchFilter("0",facet)), is("?_f=(a<0.0)"));
        assertThat(decode(filterParamUtils.setMatchFilter("1",facet)), is("?_f=(|(a<0.0)(&(a>0.0)(a<100.0)))"));
        assertThat(decode(filterParamUtils.setMatchFilter("2",facet)), is("?_f=(|(a<0.0)(a>100.0))"));
    }

    @Test
    @SneakyThrows
    public void setMatch_with_valid_bucket_ids_on_numberFacet_with_other_filterParam_yields_proper_filterParams() {
        form.add(new Parameter("_f", "(b>0.0)"));
        FieldFacet facet = new NumberFacet("a", numberFacetConfig);
        FilterParamUtils filterParamUtils = new FilterParamUtils("a", request,new LdapParser());
        assertThat(decode(filterParamUtils.setMatchFilter("0",facet)), is("?_f=(&(b>0.0)(a<0.0))"));
        assertThat(decode(filterParamUtils.setMatchFilter("1",facet)), is("?_f=(&(b>0.0)(&(a>0.0)(a<100.0)))"));
        assertThat(decode(filterParamUtils.setMatchFilter("2",facet)), is("?_f=(&(b>0.0)(a>100.0))"));
    }

    @Test
    @SneakyThrows
    public void reduceMatch_with_valid_bucket_ids_on_numberFacet_without_existing_filterParam_yields_proper_filterParams() {
        FieldFacet facet = new NumberFacet("a", numberFacetConfig);
        FilterParamUtils filterParamUtils = new FilterParamUtils("a", request,new LdapParser());
        assertThat(decode(filterParamUtils.reduceMatchFilter("0",facet,null)), is("?_f=(a<0.0)"));
//        assertThat(decode(filterParamUtils.reduceMatchFilter("1",facet,null)), is("?_f=(a<0.0)"));
//        assertThat(decode(filterParamUtils.reduceMatchFilter("2",facet,null)), is("?_f=(a<0.0)"));
    }

    @Test
    @SneakyThrows
    public void reduceMatch_with_valid_bucket_ids_on_numberFacet_with_existing_filterParam_yields_proper_filterParams() {
        form.add(new Parameter("_f", "(a<0.0)"));
        FieldFacet facet = new NumberFacet("a", numberFacetConfig);
        FilterParamUtils filterParamUtils = new FilterParamUtils("a", request,new LdapParser());
        assertThat(decode(filterParamUtils.reduceMatchFilter("0",facet,null)), is(""));
        assertThat(decode(filterParamUtils.reduceMatchFilter("1",facet,null)), is("?_f=(a<0.0)"));
        assertThat(decode(filterParamUtils.reduceMatchFilter("2",facet,null)), is("?_f=(a<0.0)"));
    }

    @Test
    @SneakyThrows
    public void reduceMatch_with_valid_bucket_ids_on_numberFacet_with_other_filterParam_yields_proper_filterParams() {
        form.add(new Parameter("_f", "(b>0.0)"));
        FieldFacet facet = new NumberFacet("a", numberFacetConfig);
        FilterParamUtils filterParamUtils = new FilterParamUtils("a", request,new LdapParser());
        assertThat(decode(filterParamUtils.reduceMatchFilter("0",facet,null)), is("?_f=(b>0.0)"));
        assertThat(decode(filterParamUtils.reduceMatchFilter("1",facet,null)), is("?_f=(b>0.0)"));
        assertThat(decode(filterParamUtils.reduceMatchFilter("2",facet,null)), is("?_f=(b>0.0)"));
    }

    @Test
    @SneakyThrows
    public void reduceMatch_with_valid_bucket_ids_on_numberFacet_with_existing_partial_filterParam_yields_proper_filterParams() {
        form.add(new Parameter("_f", "(|(a<0.0)(&(a>0.0)(a<100.0)))"));
        FieldFacet facet = new NumberFacet("a", numberFacetConfig);
        FilterParamUtils filterParamUtils = new FilterParamUtils("a", request,new LdapParser());
//        assertThat(decode(filterParamUtils.reduceMatchFilter("0",facet,null)), is("?_f=(&(a>0.0)(a<100.0))"));
        assertThat(decode(filterParamUtils.reduceMatchFilter("1",facet,null)), is("?_f=(a<0.0)"));
//        assertThat(decode(filterParamUtils.reduceMatchFilter("2",facet,null)), is("?_f=(|(a<0.0)(&(a>0.0)(a<100.0)))"));
    }

    // === YearFacet Tests ====================================

    @Test
    @SneakyThrows
    public void setMatch_with_valid_bucket_ids_on_yearFacet_without_existing_filterParam_yields_proper_filterParams2() {
        YearFacet facet = new YearFacet("a", yearFacetConfig);
        FilterParamUtils filterParamUtils = new FilterParamUtils("a", request, new LdapParser());
        assertThat(decode(filterParamUtils.setMatchFilter("2014",facet)), is("?_f=(a=2014)"));
        assertThat(decode(filterParamUtils.setMatchFilter("2015",facet)), is("?_f=(a=2015)"));
        assertThat(decode(filterParamUtils.setMatchFilter("2016",facet)), is("?_f=(a=2016)"));
    }

    @Test
    @SneakyThrows
    public void setMatch_with_valid_bucket_ids_on_yearFacet_with_existing_filterParam_yields_proper_filterParams2() {
        form.add(new Parameter("_f", "(a=2014)"));
        YearFacet facet = new YearFacet("a", yearFacetConfig);
        FilterParamUtils filterParamUtils = new FilterParamUtils("a", request, new LdapParser());
        assertThat(decode(filterParamUtils.setMatchFilter("2014",facet)), is("?_f=(a=2014)"));
        assertThat(decode(filterParamUtils.setMatchFilter("2015",facet)), is("?_f=(|(a=2014)(a=2015))"));
        assertThat(decode(filterParamUtils.setMatchFilter("2016",facet)), is("?_f=(|(a=2014)(a=2016))"));
    }

    @Test
    @SneakyThrows
    public void setMatch_with_valid_bucket_ids_on_yearFacet_with_other_filterParam_yields_proper_filterParams2() {
        form.add(new Parameter("_f", "(b=2012)"));
        YearFacet facet = new YearFacet("a", yearFacetConfig);
        FilterParamUtils filterParamUtils = new FilterParamUtils("a", request, new LdapParser());
        assertThat(decode(filterParamUtils.setMatchFilter("2014",facet)), is("?_f=(&(b=2012)(a=2014))"));
        assertThat(decode(filterParamUtils.setMatchFilter("2015",facet)), is("?_f=(&(b=2012)(a=2015))"));
        assertThat(decode(filterParamUtils.setMatchFilter("2016",facet)), is("?_f=(&(b=2012)(a=2016))"));
    }

 // === MatcherFacet Tests ====================================

    @Test
    @SneakyThrows
    public void setMatch_with_valid_bucket_ids_on_matcherFacet_without_existing_filterParam_yields_proper_filterParams2() {
        MatcherFacet facet = new MatcherFacet("a",Collections.emptyMap());
        FilterParamUtils filterParamUtils = new FilterParamUtils("a", request, new LdapParser());
        assertThat(decode(filterParamUtils.setMatchFilter("xxx",facet,"YYYY")), is("?_f=(a;YYYY=xxx)"));
    }

    @Test
    @SneakyThrows
    public void setMatch_with_valid_bucket_ids_on_matcherFacet_with_existing_filterParam_yields_proper_filterParams2() {
        form.add(new Parameter("_f", "(a;YYYY=xxx)"));
        MatcherFacet facet = new MatcherFacet("a",Collections.emptyMap());
        FilterParamUtils filterParamUtils = new FilterParamUtils("a", request, new LdapParser());
        assertThat(decode(filterParamUtils.setMatchFilter("xxx",facet,"YYYY")), is("?_f=(a;YYYY=xxx)"));
        assertThat(decode(filterParamUtils.setMatchFilter("yyy",facet,"YYYY")), is("?_f=(|(a;YYYY=xxx)(a;YYYY=yyy))"));
    }

    @Test
    @SneakyThrows
    public void setMatch_with_valid_bucket_ids_on_matcherFacet_with_other_filterParam_yields_proper_filterParams2() {
        form.add(new Parameter("_f", "(b=xxx)"));
        MatcherFacet facet = new MatcherFacet("a",Collections.emptyMap());
        FilterParamUtils filterParamUtils = new FilterParamUtils("a", request, new LdapParser());
        assertThat(decode(filterParamUtils.setMatchFilter("xxx",facet,"YYYY")), is("?_f=(&(b=xxx)(a;YYYY=xxx))"));
        assertThat(decode(filterParamUtils.setMatchFilter("yyy",facet,"YYYY")), is("?_f=(&(b=xxx)(a;YYYY=yyy))"));
    }


    private String decode(String string) throws UnsupportedEncodingException {
        return java.net.URLDecoder.decode(string, "UTF-8");
    }

}
