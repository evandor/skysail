package io.skysail.api.features;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class DefaultFeatureManager implements FeatureManager {

	private List<FeatureStateRepository> featureRepositories;

	public DefaultFeatureManager(List<FeatureStateRepository> featureRepositories) {
		this.featureRepositories = featureRepositories;
	}
	
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

}
