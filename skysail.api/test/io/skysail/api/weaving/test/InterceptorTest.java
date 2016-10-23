package io.skysail.api.weaving.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.skysail.api.weaving.Interceptor;

public class InterceptorTest {

    @Test
    public void delegates() {
        assertThat(Interceptor.beforeMethodInvocation("whatever"),is(true));
    }
}
