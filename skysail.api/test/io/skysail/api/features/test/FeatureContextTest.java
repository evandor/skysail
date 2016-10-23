package io.skysail.api.features.test;

import static org.hamcrest.CoreMatchers.is;
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

    @Before
    public void setup() {
        featureContext = new FeatureContext();
    }

    @Test
    public void featureContext_passes_stateRepository_to_FeatureManager() {
        FeatureStateRepository repo = Mockito.mock(FeatureStateRepository.class);
        featureContext.addFeaturesStateRepository(repo);
        FeatureManager featureManager = FeatureContext.getFeatureManager();
        assertThat(featureManager.getClass().getName(),is(DefaultFeatureManager.class.getName()));
        assertThat(featureManager.getStateRepositories().size(),is(1));
        assertThat(featureManager.getStateRepositories().get(0),is(repo));
    }
}
