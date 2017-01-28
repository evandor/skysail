package io.skysail.domain.core.repos;

import java.util.Optional;

import io.skysail.domain.Entity;
import io.skysail.domain.core.ApplicationModel;

/**
 * marker interface, typically defined like this:
 *
 * <pre><code>
 *    {@literal @}Component(immediate = true, property = "name=TheRepository")
 *    public class XYZRepository implements DbRepository {
 *
 *        {@literal @}Reference
 *        private DbService dbService;
 *
 *        (...)
 *    }
 * </code></pre>
 *
 * and used like this:
 *
 * <pre><code>
 *    {@literal @}Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=TheRepository)")
 *    private Repository repository;
 * </code></pre>
 *
 */
public interface DbRepository extends Repository {

    Class<? extends Entity> getRootEntity();

    /**
     * @param id the unique id in the skysail database
     * @return the identifiable entity
     */
    Entity findOne (String id);

    /**
     * searches for an entity with unique key different from the internal one.
     *
     * @param identifierKey
     * @param id
     * @return
     */
    Optional<Entity> findOne(String identifierKey, String id);

    Object save (Entity identifiable, ApplicationModel applicationModel);

    Object update(Entity entity, ApplicationModel applicationModel);

    void delete(Entity identifiable);
}
