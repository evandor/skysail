package io.skysail.api.validation.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.validation.Validator;

import org.junit.Ignore;
import org.junit.Test;

import io.skysail.api.validation.DefaultValidationImpl;

@Ignore
public class DefaultValidationImplTest {

    @Test
    public void testName() {
        DefaultValidationImpl defaultValidationImpl = new DefaultValidationImpl();
        Validator validator = defaultValidationImpl.getValidator();
        assertThat(validator, is(notNullValue()));
    }
}
