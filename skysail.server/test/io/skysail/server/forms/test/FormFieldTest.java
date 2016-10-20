package io.skysail.server.forms.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import io.skysail.api.links.LinkRelation;
import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Reference;
import io.skysail.server.DummySelectionProvider;
import io.skysail.server.domain.jvm.ResourceType;
import io.skysail.server.forms.FormField;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class FormFieldTest {

    public String test;

    @io.skysail.domain.html.Field(selectionProvider = DummySelectionProvider.class)
    public String testField;

    @Reference(selectionProvider = DummySelectionProvider.class)
    public String a;

    @Reference(selectionProvider = DummySelectionProvider.class)
    public String b;

    private SkysailServerResource<?> resource;

    private Request theRequest = Mockito.mock(Request.class);

	private org.restlet.data.Reference originalRef;

	private Form form;

    @Before
    public void setUp() {
        resource = new SkysailServerResource<Identifiable>() {

            @Override
            public Identifiable getEntity() {
                return null;
            }

            @Override
            public LinkRelation getLinkRelation() {
                return null;
            }

            @Override
            public ResourceType getResourceType() {
                return null;
            }

            @Override
            public Request getRequest() {
            	return theRequest;
            }
        };
        form = new Form();
        originalRef = Mockito.mock(org.restlet.data.Reference.class);
		Mockito.when(theRequest.getOriginalRef()).thenReturn(originalRef);

        Mockito.when(originalRef.hasQuery()).thenReturn(true);
        Mockito.when(originalRef.getQuery()).thenReturn(form.getQueryString());
        Mockito.when(originalRef.getQueryAsForm()).thenReturn(form);

    }

    @Test
    public void test_is_not_a_selectionProvider() throws Exception {
        Field field = FormFieldTest.class.getField("test");
        FormField formField = new FormField(field, resource);
        assertThat(formField.isSelectionProvider(), org.hamcrest.Matchers.is(false));
    }

    @Test
    public void testField_is_selectionProvider() throws Exception {
        Field field = FormFieldTest.class.getField("testField");
        FormField formField = new FormField(field, resource);
        assertThat(formField.isSelectionProvider(), is(true));
    }

    @Test
    public void testReference_is_selectionProvider() throws Exception {
        Field field = FormFieldTest.class.getField("a");
        FormField formField = new FormField(field, resource);
        assertThat(formField.isSelectionProvider(), is(true));
    }

    @Test
    public void testField_get_selectionOptions() throws Exception {
        Field field = FormFieldTest.class.getField("testField");
        FormField formField = new FormField(field, resource);
        assertThat(formField.getSelectionProviderOptions().size(), is(1));
    }

    @Test
    public void testReference_gets_selectionOptions() throws Exception {
        Field field = FormFieldTest.class.getField("a");
        FormField formField = new FormField(field, resource);
        assertThat(formField.getSelectionProviderOptions().size(), is(1));
    }

    @Test
	public void toggle_sort_link_toggles_from_empty_to_ASC() throws Exception {
        Field field = FormFieldTest.class.getField("a");
        Mockito.when(originalRef.hasQuery()).thenReturn(false);

        FormField formField = new FormField(field, resource);

        assertThat(formField.getToggleSortLink(), is("?_s=a%3BASC"));

	}

    @Test
   	public void toggle_sort_link_toggles_from_ASC_to_DESC() throws Exception {
           Field field = FormFieldTest.class.getField("a");
           form.add(new Parameter("_s", "a;ASC"));

           FormField formField = new FormField(field, resource);

           assertThat(formField.getToggleSortLink(), is("?_s=a%3BDESC"));
   	}

    @Test
   	public void toggle_sort_link_toggles_from_DESC_to_empty() throws Exception {
           Field field = FormFieldTest.class.getField("a");
           form.add(new Parameter("_s", "a;DESC"));

           Mockito.when(originalRef.hasQuery()).thenReturn(true);
           Mockito.when(originalRef.getQuery()).thenReturn(form.getQueryString());
           Mockito.when(originalRef.getQueryAsForm()).thenReturn(form);

           FormField formField = new FormField(field, resource);

           assertThat(formField.getToggleSortLink(), is(""));
   	}
}
