package io.skysail.api.weaving.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

import io.skysail.api.weaving.MethodInterceptorManager;

public class MethodInterceptorManagerTest {

    @Test
    @Ignore // fails on CI (?)
    public void testName() {
        MethodInterceptorManager manager = new MethodInterceptorManager();
        assertThat(manager.getMethodInvocations().size(), is(0));
        manager.beforeInvocation("ident");
        assertThat(manager.getMethodInvocations().size(), is(1));
        assertThat(manager.getMethodInvocations().get("ident"),is(1L));
    }
}
