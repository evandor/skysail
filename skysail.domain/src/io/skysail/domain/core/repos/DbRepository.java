package io.skysail.domain.core.repos;

import java.util.Optional;

import io.skysail.domain.Identifiable;
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

    Class<? extends Identifiable> getRootEntity();

    /**
     * @param id the unique id in the skysail database
     * @return the identifiable entity
     */
    Identifiable findOne (String id);

    /**
     * searches for an entity with unique key different from the internal one.
     *
     * @param identifierKey
     * @param id
     * @return
     */
    Optional<Identifiable> findOne(String identifierKey, String id);

    Object save (Identifiable identifiable, ApplicationModel applicationModel);

    Object update(Identifiable entity, ApplicationModel applicationModel);

    void delete(Identifiable identifiable);
}
