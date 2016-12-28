package io.skysail.server.http.impl;

import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.service.component.ComponentContext;

import io.skysail.server.http.HttpServer;
import io.skysail.server.http.ServerConfig;

@Ignore
public class HttpServerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testName() throws Exception {
        HttpServer httpServer = new HttpServer();
        ComponentContext componentContext = Mockito.mock(ComponentContext.class);
        httpServer.activate(new ServerConfig() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public int port() {
                return 0;
            }

            @Override
            public String productName() {
                return "skysail";
            }
        }, componentContext);
    }

}
