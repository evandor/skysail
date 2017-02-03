package io.skysail.server.app.designer.codegen.writer.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.jar.Attributes;

import org.junit.Before;
import org.junit.Test;

import io.skysail.server.app.designer.codegen.writer.AttributesWriter;

public class AttributesWriterTest {

    private AttributesWriter writer;

    @Before
    public void setUp() {
        writer = new AttributesWriter("Import-Package");
    }
    @Test
    public void emptyWriterWithName_yields_attributesName() {
        assertThat(writer.getAttributesName(),is(new Attributes.Name("Import-Package")));
    }

    @Test
    public void emptyWriterWithName_yields_emptyAttributesValue() {
        assertThat(writer.getAttributesValue(),is(""));
    }

    @Test
    public void adding_versions_does_not_change_attributes_name() {
        writer.addVersion("io.skysail.server.security.config", "[0.3,1)");
        assertThat(writer.getAttributesName(),is(new Attributes.Name("Import-Package")));
    }

    @Test
    public void writer_yields_single_attributesValue_provided_with_addVersion() {
        writer.addVersion("io.config", "[0.3,1)");
        assertThat(writer.getAttributesValue(),is("io.config;version=\"[0.3,1)\""));
    }

    @Test
    public void writer_yields_multiple_attributesValues_provided_with_addVersion_in_given_order() {
        writer
            .addVersion("io.config", "[0.3,1)")
            .addVersion("io.other", "[0.7,1)");
        assertThat(writer.getAttributesValue(),is("io.config;version=\"[0.3,1)\",io.other;version=\"[0.7,1)\""));
    }

    @Test
    public void writer_accepts_packages_without_versions() {
        writer
            .addVersion("io.config", null)
            .addVersion("io.other", "[0.7,1)");
        assertThat(writer.getAttributesValue(),is("io.config,io.other;version=\"[0.7,1)\""));
    }

    //"io.skysail.server.security.config;version=\"[0.3,1)\"

//    "io.skysail.server,"
//    + "io.skysail.server.domain.jvm;version=\"[0.2,1)\"
}
