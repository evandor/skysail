package io.skysail.server.domain.jvm.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import io.skysail.server.domain.jvm.SkysailFieldModel;

public class SkysailFieldModelTest {

    private SkysailFieldModel classFieldModel;

    @io.skysail.domain.html.Field
    private String aField;

    @Before
    public void setUp() throws Exception {
        Field field = this.getClass().getDeclaredField("aField");
        classFieldModel = new SkysailFieldModel(null, null, field);
    }

    @Test
    @Ignore
    public void toString_is_formatted_nicely() {
        String[] toString = classFieldModel.toString().split("\\n");
        assertThat(toString[0],is("SkysailFieldModel(id=aField, type=String, inputType=TEXT)"));
    }
}
