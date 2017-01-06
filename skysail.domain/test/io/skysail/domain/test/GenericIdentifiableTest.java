package io.skysail.domain.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.skysail.domain.GenericIdentifiable;

public class GenericIdentifiableTest {

    @Test
    public void default_constructor_yields_null_payload_with_id() {
        GenericIdentifiable identifiable = new GenericIdentifiable();
        assertThat(identifiable.getId(),is(notNullValue()));
        assertThat(identifiable.getPayload(),is(nullValue()));
    }

    @Test
    public void constructor_sets_passed_value() {
        GenericIdentifiable identifiable = new GenericIdentifiable("something");
        assertThat(identifiable.getId(),is(notNullValue()));
        assertThat(identifiable.getPayload(),is("something"));
    }

    @Test
    public void toString_contains_id_and_paylod() {
        GenericIdentifiable identifiable = new GenericIdentifiable("something");
        assertThat(identifiable.toString(),containsString("something"));
        assertThat(identifiable.toString(),containsString("id=1"));
    }

}
