package io.skysail.server.restlet.resources.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Form;

import io.skysail.domain.Entity;
import io.skysail.server.restlet.resources.FormDeserializer;

public class FormDeserializerTest {

    private FormDeserializer<Entity> deserializer;

    @Before
    public void setUp() throws Exception {
        Class<AnEntity> parameterizedType = AnEntity.class;
        deserializer = new FormDeserializer<>(parameterizedType);
    }

    @Test
    public void createFrom() {
        Form form = new Form();
        Entity entity = deserializer.createFrom(form);
        assertThat(entity.getId(),is(nullValue()));
    }

}
