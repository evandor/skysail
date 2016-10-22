/**
 * Root of the feature toggles API.
 *
 * <p>{@link <a href="http://martinfowler.com/articles/feature-toggles.html">Feature toggles</a>} are a technique to
 * alter system behavior without changing code.</p>
 *
 * A typical example could look like this:
 *
 * <pre>
 *   public enum SecurityFeatures implements Feature {
 *
 *     &#64;Label("toggle usage of the cache for user credentials")
 *     USE_CREDENTIALS_CACHE_FEATURE;
 *
 *     public boolean isActive() {
 *       return FeatureContext.getFeatureManager().isActive(this);
 *     }
 *   }
 * </pre>
 *
 * Create an enum (implementing Feature) with an "isActive" method like above and use it like this:
 *
 * <pre>
 *   if (SecurityFeatures.USE_CREDENTIALS_CACHE_FEATURE.isActive()) {
 *     ...
 *   }
 * </pre>
 *
 * Now, in the configuration (a file called 'features.cfg' to be put in the config directory)
 * you could define something like this:
 *
 * <pre>
 *   io.skysail.server.features.SecurityFeatures.USE_CREDENTIALS_CACHE_FEATURE = true
 * </pre>
 *
 * to switch on "USE_CREDENTIALS_CACHE_FEATURE" for all users. Or, you can define
 * some more logic, using a strategy:
 *
 * <pre>
 *   io.skysail.server.features.SecurityFeatures.USE_CREDENTIALS_CACHE_FEATURE = true
 *   io.skysail.server.features.SecurityFeatures.strategy = username
 *   io.skysail.server.features.SecurityFeatures.param.users = admin, dev
 * </pre>
 *
 * Using different strategies, you can switch on the feature at a given date,
 * restrict it it IP addresses, or apply any custom logic you might need.
 *
 * The logic to evaluate the configuration is handled in io.skysail.server.features.repositories.ConfigAdminFeatureStateRepository.
 */
@Version("0.3.1")
package io.skysail.api.features;

import org.osgi.annotation.versioning.Version;
