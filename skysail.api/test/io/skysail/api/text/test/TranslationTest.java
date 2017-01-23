package io.skysail.api.text.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.skysail.api.text.Translation;
import io.skysail.api.text.TranslationStore;

public class TranslationTest {

    private TranslationStore store;

    @Before
    public void setUp() {
        store = Mockito.mock(TranslationStore.class);
    }

    @Test
    public void testFirstConstructor() {
        Translation translation = new Translation("text", store, Locale.getDefault());
        assertThat(translation.getLocale(),is(Locale.getDefault()));
        assertThat(translation.getMessageArguments().size(), is(0));
    }

    @Test
    public void testSecondConstructor() {
        Collection<Object> messageArguments = new ArrayList<>();
        Translation translation = new Translation("text", store, Locale.getDefault(), messageArguments);
        assertThat(translation.getLocale(),is(Locale.getDefault()));
    }

}
