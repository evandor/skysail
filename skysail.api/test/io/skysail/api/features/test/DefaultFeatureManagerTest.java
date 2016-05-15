package io.skysail.api.features.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.skysail.api.features.DefaultFeatureManager;
import io.skysail.api.features.Feature;
import io.skysail.api.features.FeatureState;
import io.skysail.api.features.FeatureStateRepository;

public class DefaultFeatureManagerTest {

    private DefaultFeatureManager featureManager;

    private Feature feature;

    @Before
    public void setup() {
        List<FeatureStateRepository> featureStateRepositories = new ArrayList<>();
        featureManager = new DefaultFeatureManager(featureStateRepositories);
        feature = Mockito.mock(Feature.class);
    }

    @Test
    public void testName() {
        assertThat(featureManager.getName(),is(nullValue()));
    }

    @Test
    public void testCurrentFeatureUser() {
        assertThat(featureManager.getCurrentFeatureUser(),is(nullValue()));
    }

    @Test
    public void testFeatureState() {
        assertThat(featureManager.getFeatureState(null),is(nullValue()));
    }

    @Test
    public void testFeatures() {
        assertThat(featureManager.getFeatures().size(),is(0));
    }

    @Test
    public void never_active_if_no_featureStateRepositories_are_defined() {
        assertThat(featureManager.isActive(feature),is(false));
    }

    @Test
    public void default_featureState_yields_not_active() {
        List<FeatureStateRepository> repos = new ArrayList<>();
        FeatureStateRepository featureStateRepository = Mockito.mock(FeatureStateRepository.class);
        Mockito.when(featureStateRepository.getFeatureState(feature)).thenReturn(new FeatureState(feature));
        repos.add(featureStateRepository);
        featureManager = new DefaultFeatureManager(repos);
        assertThat(featureManager.isActive(feature),is(false));
    }

    @Test
    public void active_featureState_yields_active() {
        List<FeatureStateRepository> repos = new ArrayList<>();
        FeatureStateRepository featureStateRepository = Mockito.mock(FeatureStateRepository.class);
        FeatureState featureState = new FeatureState(feature);
        featureState.setEnabled(true);
        Mockito.when(featureStateRepository.getFeatureState(feature)).thenReturn(featureState);
        repos.add(featureStateRepository);
        featureManager = new DefaultFeatureManager(repos);
        assertThat(featureManager.isActive(feature),is(true));
    }
}
