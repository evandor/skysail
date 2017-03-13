package io.skysail.server.converter.impl.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.core.app.SkysailApplication;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.server.converter.HtmlConverter;
import io.skysail.server.converter.impl.StringTemplateRenderer;

@RunWith(MockitoJUnitRunner.class)
public class StringTemplateRendererTest {

    @Mock
    private HtmlConverter htmlConverter;

    @Mock
    private Bundle skysailServerConverterBundle;

    @Mock
    private BundleContext bundleContext;

    private SkysailServerResource testResource = new SkysailServerTestResource();

    @InjectMocks
    private StringTemplateRenderer stRenderer = new StringTemplateRenderer(htmlConverter, testResource);

    private Variant variant;

    @Before
    public void setUp() throws MalformedURLException {
        // testResourceInThisDirectory = new SkysailServerTestResource();
        // testResourceInThisDirectory.setApplication(skysailApplication);
        variant = new Variant(MediaType.TEXT_HTML);

        @SuppressWarnings("deprecation")
        URL url = new File("test").toURL();

        SkysailApplication anApplication = new SkysailApplication("testApp") {
            @Override
            public Bundle getBundle() {
                return skysailServerConverterBundle;
            }
        };
        when(testResource.getApplication()).thenReturn(anApplication);
       // when(bundleContext.getBundle()).thenReturn(skysailServerConverterBundle);
        when(skysailServerConverterBundle.getBundleContext()).thenReturn(bundleContext);
        when(skysailServerConverterBundle.getSymbolicName()).thenReturn("skysail.server.converter");
        when(skysailServerConverterBundle.getResource("/templates")).thenReturn(url);
        when(skysailServerConverterBundle.getHeaders()).thenReturn(new Hashtable<>());
        when(bundleContext.getBundles()).thenReturn(new Bundle[] { skysailServerConverterBundle });
    }

    @Test
    @Ignore
    public void content_is_read_from_associated_indexStg_file() {
        StringRepresentation representation = stRenderer.createRepresenation(new SkysailResponse<>(), variant,
                testResource);
        assertThat(representation.getText(), is("~content~"));
    }

}
