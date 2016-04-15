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
            manager = new FeatureManager() {

                @Override
                public List<FeatureStateRepository> getStateRepositories() {
                    return featureRepositories;
                }

                @Override
                public String getName() {
                    return null;
                }

                @Override
                public FeatureUser getCurrentFeatureUser() {
                    return null;
                }

                @Override
                public boolean isActive(Feature feature) {
                    Optional<FeatureState> featureState = featureRepositories.stream().map(repo -> 
                        repo.getFeatureState(feature)
                    ).filter(state -> 
                        state != null
                    ).findFirst();

                    if (!featureState.isPresent()) {
                        return false;
                    }
                    FeatureState state = featureState.get();
                    if (state.isEnabled()) {
                        return true;
                    }

                    return false;

                }

                @Override
                public FeatureState getFeatureState(Feature feature) {
                    return null;
                }

                @Override
                public void setFeatureState(FeatureState state) { // NOSONAR
                	
                }

                @Override
                public Set<Feature> getFeatures() {
                    return Collections.emptySet();
                }
            };
        }
        return manager;
    }
}
