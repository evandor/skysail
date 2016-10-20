package io.skysail.server.utils.test;

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

import io.skysail.server.utils.ParamsUtils;

public class ParamsUtilsTest {

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
    public void toggle_sort_link_toggles_from_empty_to_ASC() throws Exception {
        when(originalRef.hasQuery()).thenReturn(false);
        assertThat(ParamsUtils.toggleSortLink(theRequest, "a"), is("?_s=" + encode("a;ASC")));
    }

    @Test
    public void toggle_sort_link_toggles_from_ASC_to_DESC() throws Exception {
        form.add(new Parameter("_s", "a;ASC"));
        assertThat(ParamsUtils.toggleSortLink(theRequest, "a"), is("?_s=" + encode("a;DESC")));
    }

    @Test
    public void toggle_sort_link_toggles_from_DESC_to_empty() throws Exception {
        form.add(new Parameter("_s", "a;DESC"));
        assertThat(ParamsUtils.toggleSortLink(theRequest, "a"), is(""));
    }

    @Test
    public void toggle_second_sort_link_toggles_from_empty_to_ASC() throws Exception {
        form.add(new Parameter("_s", "a;ASC"));
        assertThat(ParamsUtils.toggleSortLink(theRequest, "b"), is("?_s=" + encode("a;ASC,b;ASC")));
    }

    @Test
    public void toggle_second_sort_link_toggles_from_ASC_to_DESC() throws Exception {
        form.add(new Parameter("_s", "a;ASC,b;ASC"));
        assertThat(ParamsUtils.toggleSortLink(theRequest, "b"), is("?_s=" + encode("a;ASC,b;DESC")));
    }

    @Test
    public void toggle_second_sort_link_toggles_from_DESC_to_empty() throws Exception {
        form.add(new Parameter("_s", "a;ASC,b;DESC"));
        assertThat(ParamsUtils.toggleSortLink(theRequest, "b"), is("?_s=" + encode("a;ASC")));
    }

    @Test
    public void toggle_additional_sort_link_toggles_from_empty_to_ASC() throws Exception {
        form.add(new Parameter("_filter", "(buchungstag%3D2016)"));
        assertThat(ParamsUtils.toggleSortLink(theRequest, "a"), is("?_filter=" + encode("(buchungstag%3D2016)") + "&_s=" + encode("a;ASC")));
    }

    @Test
    public void toggle_additional_sort_link_toggles_from_ASC_to_DESC() throws Exception {
        form.add(new Parameter("_filter", "(buchungstag%3D2016)"));
        form.add(new Parameter("_s", "a;ASC"));
        assertThat(ParamsUtils.toggleSortLink(theRequest, "a"), is("?_filter=" + encode("(buchungstag%3D2016)") + "&_s=" + encode("a;DESC")));
    }

    @Test
    public void toggle_additional_sort_link_toggles_from_DESC_to_empty() throws Exception {
        form.add(new Parameter("_filter", "(buchungstag%3D2016)"));
        form.add(new Parameter("_s", "a;DESC"));
        assertThat(ParamsUtils.toggleSortLink(theRequest, "a"), is("?_filter=" + encode("(buchungstag%3D2016)")));
    }

    private String encode(String string) throws UnsupportedEncodingException {
        return java.net.URLEncoder.encode(string, "UTF-8");
    }

}