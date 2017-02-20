package io.skysail.server.restlet.resources.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Form;

import io.skysail.server.restlet.resources.FormDeserializer;

public class FormDeserializerTest {

    private FormDeserializer<AnEntity> deserializer;

    private Form form;

    @Before
    public void setUp() throws Exception {
        Class<AnEntity> parameterizedType = AnEntity.class;
        deserializer = new FormDeserializer<>(parameterizedType);
        form = new Form();
    }

    @Test
    public void deserialising_null_yields_null_entity() {
        AnEntity entity = deserializer.createFrom(null);
        assertThat(entity,is(nullValue()));
    }

    @Test
    public void deserialises_empty_form() {
        AnEntity entity = deserializer.createFrom(form);
        assertThat(entity.getId(),is(nullValue()));
    }

    @Test
    public void deserialzes_simple_form() {
        form.add("id", "17");
        form.add("name", "theName");

        AnEntity entity = deserializer.createFrom(form);

        assertThat(entity.getId(),is("17"));
        assertThat(entity.getName(),is("theName"));
    }

    @Test
    public void ignores_submit_field() {
        form.add("id", "17");
        form.add("name", "theName");
        form.add("submit", "submit");

        deserializer.createFrom(form);
    }

    @Test
    public void deserialzes_complex_form() {
        form.add(AnEntity.class.getName() + "|id", "17");
        form.add(AnEntity.class.getName() + "|name", "theName");

        AnEntity entity = deserializer.createFrom(form);

        assertThat(entity.getId(),is("17"));
        assertThat(entity.getName(),is("theName"));
    }

}
