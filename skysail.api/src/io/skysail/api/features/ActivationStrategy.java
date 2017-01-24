package io.skysail.api.features;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Implementations define a concrete strategy under which circumstances the
 * feature will be active.
 *
 */
@ProviderType
public interface ActivationStrategy {

    String getId();

    String getName();

    boolean isActive(FeatureState state, String user);
}