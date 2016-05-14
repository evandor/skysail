/**
 * Root of the feature toggles API.
 *
 * @see <a href="http://martinfowler.com/articles/feature-toggles.html">Feature toggles</a> are a technique to
 * alter system behavior without changing code.
 *
 * A typical example could look like this:
 *
 * {@code
 *   public enum SecurityFeatures implements Feature {
 *
 *     {@literal@}Label("toggle usage of the cache for user credentials")
 *     USE_CREDENTIALS_CACHE_FEATURE;
 *
 *     public boolean isActive() {
 *       return FeatureContext.getFeatureManager().isActive(this);
 *     }
 *   }
 * }
 *
 * Create an enum implementing {@link Feature} with an "isActive" method like above and use it like this:
 *
 * <pre><code>
 * if (SecurityFeatures.USE_CREDENTIALS_CACHE_FEATURE.isActive()) {
 *     ...
 * }
 * </code></pre>
 *
 * Now, in the configuration you could define something like this:
 *
 * <pre><code>
 * io.skysail.server.features.SecurityFeatures.USE_CREDENTIALS_CACHE_FEATURE = true
 * </code></pre>
 *
 * to switch on "USE_CREDENTIALS_CACHE_FEATURE" for all users. Or, you can define
 * some more logic, using a strategy:
 *
 * <pre><code>
 * io.skysail.server.features.SecurityFeatures.USE_CREDENTIALS_CACHE_FEATURE = true
 * io.skysail.server.features.SecurityFeatures.strategy = username
 * io.skysail.server.features.SecurityFeatures.param.users = admin, dev
 * </code></pre>
 *
 * Using different strategies, you can switch on the feature at a given date,
 * restrict it it IP addresses, or apply any custom logic you might need.
 */
@Version("0.3.1")
package io.skysail.api.features;

import org.osgi.annotation.versioning.Version;
