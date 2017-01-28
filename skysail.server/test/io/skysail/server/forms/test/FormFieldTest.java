package io.skysail.server.forms.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.data.Form;

import io.skysail.api.links.LinkRelation;
import io.skysail.domain.GenericIdentifiable;
import io.skysail.domain.Entity;
import io.skysail.domain.html.InputType;
import io.skysail.server.domain.jvm.ResourceType;
import io.skysail.server.forms.FormField;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class FormFieldTest {

    public String test;

    @io.skysail.domain.html.Field
    public String simpleString;

    @io.skysail.domain.html.Field
    public Date simpleDate;

    @io.skysail.domain.html.Field(inputType = InputType.TEXTAREA)
    public String textarea;

    @io.skysail.domain.html.Field
    public List<GenericIdentifiable> listCollection;

    @io.skysail.domain.html.Field
    public Set<GenericIdentifiable> setCollection;

    private SkysailServerResource<?> resource;

    private Request theRequest = Mockito.mock(Request.class);

    private org.restlet.data.Reference originalRef;

    private Form form;

    @Before
    public void setUp() {
        resource = new SkysailServerResource<Entity>() {

            @Override
            public Entity getEntity() {
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
        when(theRequest.getOriginalRef()).thenReturn(originalRef);

        when(originalRef.hasQuery()).thenReturn(true);
        when(originalRef.getQuery()).thenReturn(form.getQueryString());
        when(originalRef.getQueryAsForm()).thenReturn(form);
        when(originalRef.getHierarchicalPart()).thenReturn("");
    }

    @Test
    public void simple_String_setup_works() {
        FormField formField = createFormField("simpleString");

        assertThat(formField.getId()).isEqualTo("io.skysail.server.forms.test.FormFieldTest|simpleString");
        //assertThat(formField.getName()).isSameAs("simpleString");
        assertThat(formField.getInputType().name()).isSameAs(InputType.TEXT.name());
        //        assertThat(formField.getEntityClass(),is(("")));
        assertThat(formField.getType().getName()).isSameAs(String.class.getName());
        assertThat(formField.getEntityType().getTypeName()).isSameAs(String.class.getName());
    }

    @Test
    public void simple_Date_setup_works() {
        FormField formField = createFormField("simpleDate");

        assertThat(formField.getId()).isEqualTo("io.skysail.server.forms.test.FormFieldTest|simpleDate");
       // assertThat(formField.getName()).isSameAs("simpleDate");
        assertThat(formField.getInputType().name()).isSameAs(InputType.TEXT.name());
        assertThat(formField.getType().getName()).isSameAs(Date.class.getName());
        assertThat(formField.getEntityType().getTypeName()).isSameAs(Date.class.getName());
    }

    @Test
    public void textarea_setup_works() {
        FormField formField = createFormField("textarea");
        assertThat(formField.getInputType().name()).isSameAs(InputType.TEXTAREA.name());
        assertThat(formField.getType().getName()).isSameAs(String.class.getName());
    }

    @Test
    public void listCollection_setup_works() {
        FormField formField = createFormField("listCollection");
        assertThat(formField.getInputType().name()).isSameAs(InputType.TEXT.name());
        assertThat(formField.getType().getName()).isSameAs(List.class.getName());
        assertThat(formField.getEntityType().getTypeName()).isSameAs(GenericIdentifiable.class.getName());
    }

    @Test
    public void setCollection_setup_works() {
        FormField formField = createFormField("setCollection");
        assertThat(formField.getInputType().name()).isSameAs(InputType.TEXT.name());
        assertThat(formField.getType().getName()).isSameAs(Set.class.getName());
    }

    private FormField createFormField(String name) {
        try {
            return new FormField(FormFieldTest.class.getField(name), resource.getCurrentEntity(), null);
        } catch (NoSuchFieldException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

//    @Test
//    public void test_is_not_a_selectionProvider() throws Exception {
//        Field field = FormFieldTest.class.getField("test");
//        FormField formField = new FormField(field, resource.getCurrentEntity(), null);
//        assertThat(formField.isSelectionProvider(), org.hamcrest.Matchers.is(false));
//    }
//
//    @Test
//    public void testReference_is_selectionProvider() throws Exception {
//        Field field = FormFieldTest.class.getField("a");
//        FormField formField = new FormField(field, resource.getCurrentEntity(), null);
//        assertThat(formField.isSelectionProvider(), is(true));
//    }

    // @Test
    // public void testReference_gets_selectionOptions() throws Exception {
    // Field field = FormFieldTest.class.getField("a");
    // FormField formField = new FormField(field, resource);
    // assertThat(formField.getSelectionProviderOptions().size(), is(1));
    // }

    // @Test
    // public void toggle_sort_link_toggles_from_empty_to_ASC() throws Exception
    // {
    // Field field = FormFieldTest.class.getField("a");
    // Mockito.when(originalRef.hasQuery()).thenReturn(false);
    //
    // FormField formField = new FormField(field, resource.getCurrentEntity());
    //
    // assertThat(formField.getToggleSortLink(), is("?_s=a%3BASC"));
    //
    // }
    //
    // @Test
    // public void toggle_sort_link_toggles_from_ASC_to_DESC() throws Exception
    // {
    // Field field = FormFieldTest.class.getField("a");
    // form.add(new Parameter("_s", "a;ASC"));
    //
    // FormField formField = new FormField(field, resource.getCurrentEntity());
    //
    // assertThat(formField.getToggleSortLink(), is("?_s=a%3BDESC"));
    // }

    // @Test
    // public void toggle_sort_link_toggles_from_DESC_to_empty() throws
    // Exception {
    // Field field = FormFieldTest.class.getField("a");
    // form.add(new Parameter("_s", "a;DESC"));
    //
    // Mockito.when(originalRef.hasQuery()).thenReturn(true);
    // Mockito.when(originalRef.getQuery()).thenReturn(form.getQueryString());
    // Mockito.when(originalRef.getQueryAsForm()).thenReturn(form);
    //
    // FormField formField = new FormField(field, resource.getCurrentEntity());
    //
    // assertThat(formField.getToggleSortLink(), is(""));
    // }
}
