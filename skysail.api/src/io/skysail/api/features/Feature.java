package io.skysail.api.features;

import java.io.Serializable;

/**
 *
 * <p>
 * This interface represents a feature and is typically implemented by a feature
 * enum.
 * </p>
 *
 * <p>
 * Usually it makes sense to implement the following method which allows a very
 * easy way to check the status of a feature.
 * </p>
 *
 * <pre>
 *   public boolean isActive() {
 *     return FeatureContext.getFeatureManager().isActive(this);
 *   }
 * </pre>
 *
 * <p>
 * Based on togglz library (http://www.togglz.org/).
 * </p>
 *
 *
 */
@FunctionalInterface
public interface Feature extends Serializable {

    /**
     * Returns a textual representation of the feature.
     *
     * This method is implicitly implemented as features typically are enumerations and
     * therefore provide such a method automatically.
     *
     */
    String name();

}
