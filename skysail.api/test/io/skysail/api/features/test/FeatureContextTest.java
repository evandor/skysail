package io.skysail.api.features.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.skysail.api.features.DefaultFeatureManager;
import io.skysail.api.features.FeatureContext;
import io.skysail.api.features.FeatureManager;
import io.skysail.api.features.FeatureStateRepository;

public class FeatureContextTest {

    private FeatureContext featureContext;
    private FeatureStateRepository repo;

    @Before
    public void setup() {
        featureContext = new FeatureContext();
        repo = Mockito.mock(FeatureStateRepository.class);
        featureContext.addFeaturesStateRepository(repo);
    }

    @Test
    public void featureContext_passes_stateRepository_to_FeatureManager() {
        FeatureManager featureManager = FeatureContext.getFeatureManager();
        assertThat(featureManager.getClass().getName(),is(DefaultFeatureManager.class.getName()));
        assertThat(featureManager.getStateRepositories().size(),is(1));
        assertThat(featureManager.getStateRepositories().get(0),is(repo));
    }

    @Test
    public void featureManager_is_the_same_on_second_call() {
        FeatureManager featureManager = FeatureContext.getFeatureManager();
        assertThat(FeatureContext.getFeatureManager(),is(featureManager));
    }

    @Test
    public void featureContext_passes_stateRepository_to_FeatureManager2() {
        featureContext.removeFeaturesStateRepository(repo);
        FeatureManager featureManager = FeatureContext.getFeatureManager();
        assertThat(featureManager.getClass().getName(),is(DefaultFeatureManager.class.getName()));
        assertThat(featureManager.getStateRepositories(),is(notNullValue()));
    }

}
