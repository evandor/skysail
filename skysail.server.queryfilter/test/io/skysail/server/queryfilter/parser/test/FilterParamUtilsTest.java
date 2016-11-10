package io.skysail.server.queryfilter.parser.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;

import io.skysail.server.queryfilter.parser.Parser;
import io.skysail.server.utils.params.FilterParamUtils;

public class FilterParamUtilsTest {

    private Reference originalRef;
    private Form form;
    private Request theRequest = mock(Request.class);

    @Before
    public void setUp() {
        form = new Form();
        originalRef = Mockito.mock(org.restlet.data.Reference.class);
        when(theRequest.getOriginalRef()).thenReturn(originalRef);

        when(originalRef.hasQuery()).thenReturn(true);
        when(originalRef.getQuery()).thenReturn(form.getQueryString());
        when(originalRef.getQueryAsForm()).thenReturn(form);
        when(originalRef.getHierarchicalPart()).thenReturn("");
    }

    @Test
    public void setMatch_on_second_filterKey_ANDS_filter_param() throws UnsupportedEncodingException {
        form.add(new Parameter("_f", "(a=b)"));
        assertThat(decode(new FilterParamUtils("x", theRequest,new Parser()).setMatchFilter("y",null)), is("?_f=(&(a=b)(x=y))"));
    }

    @Test
    public void setMatch_on_same_filterKey_ORS_filter_param() throws UnsupportedEncodingException {
        form.add(new Parameter("_f", "(a=b)"));
        assertThat(decode(new FilterParamUtils("a", theRequest, new Parser()).setMatchFilter("c",null)), is("?_f=(|(a=b)(a=c))"));
    }

    private String encode(String string) throws UnsupportedEncodingException {
        return java.net.URLEncoder.encode(string, "UTF-8");
    }

    private String decode(String string) throws UnsupportedEncodingException {
        return java.net.URLDecoder.decode(string, "UTF-8");
    }

}
