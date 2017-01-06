package io.skysail.domain.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.skysail.domain.EmptyIdentifiable;

public class EmptyIdentifiableTest {

    @Test
    public void testName() {
        EmptyIdentifiable identifiable = new EmptyIdentifiable();
        assertThat(identifiable.getId(), is(nullValue()));
    }

    @Test
    public void toString_contains_id_and_paylod() {
        EmptyIdentifiable identifiable = new EmptyIdentifiable();
        assertThat(identifiable.toString(),containsString("EmptyIdentifiable(id=null"));
    }

}
