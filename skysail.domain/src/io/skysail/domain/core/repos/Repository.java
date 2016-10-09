package io.skysail.domain.core.repos;

import java.util.Optional;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;

public interface Repository {

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
