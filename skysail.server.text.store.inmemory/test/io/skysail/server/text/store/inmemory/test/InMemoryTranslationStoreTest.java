package io.skysail.server.text.store.inmemory.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.restlet.Request;

import io.skysail.server.text.store.inmemory.InMemoryTranslationStore;

public class InMemoryTranslationStoreTest {

    private static final String TITLE_KEY = "io.skysail.entity.Note|title";

    private InMemoryTranslationStore inMemoryTranslationStore;

    private BundleContext bundleContext = Mockito.mock(BundleContext.class);

    private ClassLoader cl = Mockito.mock(ClassLoader.class);

    private Request request = Mockito.mock(Request.class);

    @Before
    public void setUp() {
        inMemoryTranslationStore = new InMemoryTranslationStore();
        inMemoryTranslationStore.persist(TITLE_KEY, "the title", Locale.getDefault(), bundleContext);
    }

    @Test
    public void persist() {
        boolean persisted = inMemoryTranslationStore.persist("key", "message", Locale.getDefault(), bundleContext);
        assertThat(persisted, is(true));
    }

    @Test
    public void getString() {
        Optional<String> theTitleTranslated = inMemoryTranslationStore.get(TITLE_KEY);
        assertThat(theTitleTranslated.isPresent(), is(true));
        assertThat(theTitleTranslated.get(), is("the title"));
    }

    @Test
    public void getStringClassLoader() {
        Optional<String> theTitleTranslated = inMemoryTranslationStore.get(TITLE_KEY, cl);
        assertThat(theTitleTranslated.get(), is("the title"));
    }

    @Test
    public void getStringClassLoaderRequest() {
        Optional<String> theTitleTranslated = inMemoryTranslationStore.get(TITLE_KEY, cl, request);
        assertThat(theTitleTranslated.get(), is("the title"));
    }

//    @Test
//    public void getStringClassLoaderRequestLocale() {
//        Optional<String> theTitleTranslated = inMemoryTranslationStore.get("key", cl, request, Locale.getDefault());
//        assertThat(theTitleTranslated.get(), is("the title"));
//    }

}
