package io.skysail.server.utils.params.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.domain.jvm.facets.NumberFacet;
import io.skysail.server.domain.jvm.facets.YearFacet;
import io.skysail.server.utils.params.FilterParamUtils;

public class FilterParamUtilsTest {

    private Reference originalRef;
    private Form form;
    private Request theRequest = mock(Request.class);
    private Map<String, String> facetConfig;

    @Before
    public void setUp() {
        form = new Form();
        originalRef = Mockito.mock(org.restlet.data.Reference.class);
        when(theRequest.getOriginalRef()).thenReturn(originalRef);

        when(originalRef.hasQuery()).thenReturn(true);
        when(originalRef.getQuery()).thenReturn(form.getQueryString());
        when(originalRef.getQueryAsForm()).thenReturn(form);
        when(originalRef.getHierarchicalPart()).thenReturn("");

        facetConfig = new HashMap<>();
    }

    @Test
    public void setMatch_on_filter_creates_filter_param_for_numberFacet() throws UnsupportedEncodingException {
        when(originalRef.hasQuery()).thenReturn(false);
        facetConfig.put("BORDERS", "0");
        FieldFacet facet = new NumberFacet("id", facetConfig);
        assertThat(decode(new FilterParamUtils("a", theRequest,null).setMatchFilter("0",facet)), is("?_f=(a<0.0)"));
    }

    @Test
    public void setMatch_on_filter_creates_filter_param_for_yearFacet() throws UnsupportedEncodingException {
        when(originalRef.hasQuery()).thenReturn(false);
        facetConfig.put("START", "2016");
        FieldFacet facet = new YearFacet("id", facetConfig);
        assertThat(decode(new FilterParamUtils("a", theRequest,null).setMatchFilter("0",facet)), is("?_f=(a=0)"));
    }

    @Test
    public void setMatch_again_on_filter_removes_filter_param() {
        form.add(new Parameter("_f", "(a=b)"));
        FieldFacet facet = new NumberFacet("a", facetConfig);
        assertThat(new FilterParamUtils("a", theRequest,null).setMatchFilter("b",facet), is(""));
    }

    @Test
    public void setMatch_again_on_filter_removes_filter_param_for_numberFacet() throws UnsupportedEncodingException {
        form.add(new Parameter("_f", "(a<0.0)"));
        Map<String, String> facetConfig = new HashMap<>();
        facetConfig.put("BORDERS", "0,100");
        FieldFacet numberFacet = new NumberFacet("a", facetConfig);
        //FilterParser parser = new io.skysail.server.queryfilter.P
        assertThat(new FilterParamUtils("a", theRequest,null).setMatchFilter("1",numberFacet), is(""));
    }

    // === selections ===

    @Test
    public void selected_is_empty_for_empty_query() {
        when(originalRef.hasQuery()).thenReturn(false);
        //assertThat(new FilterParamUtils("a", theRequest).getSelected().size(),is(0));
    }

    @Test
    @Ignore
	public void testName() {
    	form.add(new Parameter("_f", "(buchungstag%3D2016)"));

    	//Set<String> selected = new FilterParamUtils("a", theRequest).getSelected();

    	//assertThat(selected.size(),is(1));
    	//assertThat(selected.iterator().next(),is("2016"));
	}


    private String decode(String string) throws UnsupportedEncodingException {
        return java.net.URLDecoder.decode(string, "UTF-8");
    }


}
