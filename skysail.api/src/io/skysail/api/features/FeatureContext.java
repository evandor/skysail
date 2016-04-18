package io.skysail.api.features;

import java.util.*;

import org.osgi.service.component.annotations.*;

@Component(immediate = true)
public class FeatureContext {

    private static volatile List<FeatureStateRepository> featureRepositories = new ArrayList<>();

    // for now: the one and only featureManager 
    private static FeatureManager manager;

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    public void addFeaturesStateRepository(FeatureStateRepository repo) {
        featureRepositories.add(repo);
    }

    public void removeFeaturesStateRepository(FeatureStateRepository repo) {
        featureRepositories.remove(repo);
    }

    /**
     * Returns the {@link FeatureManager} for the current application.
     *
     * @return The {@link FeatureManager} for the application, never
     *         <code>null</code>
     */
    public static synchronized FeatureManager getFeatureManager() {
        if (manager == null) {
            manager = new DefaultFeatureManager(featureRepositories);
        }
        return manager;
    }
}
