package io.skysail.domain.core;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.skysail.domain.Entity;
import lombok.Getter;
import lombok.NonNull;

/**
 * This is the root class of skysail's core domain, describing an application,
 * which aggregates entities which in turn aggregate fields.
 *
 * According to specific needs, the core domain can be adapted by extending the
 * corresponding classes. For example, there's a domain extension dealing with
 * the creation of java source files and classes according to a specific core
 * domain model.
 * 
 * This core domain is, in terms of dependencies, kept as clean as possible; not
 * even lombok or a logging framework is being used.
 *
 */
public class ApplicationModel {

    @Getter
    /** an identifier, could be a full qualified class name. */
    private final String name;

    /** the applications entities in a map with their name as key. */
    private final Map<String, EntityModel<? extends Entity>> entities = new LinkedHashMap<>();

    /** the applications entities in a map with their name as key. */
    //@Getter
    //private final Map<String, ValueObjectModel> valueobjects = new LinkedHashMap<>();

    /**
     * an applications unique name; could be a full qualified java identifier.
     */
    public ApplicationModel(String fullQualifiedClassName) {
        this.name = fullQualifiedClassName;
    }

    /**
     * adds an non-null entity model identified by its id.
     *
     * If an entity model with the same name exists already, this method
     * returns silently.
     *
     * Otherwise, the entity will be added and the current application will be set
     * in the entity.
     */
    public <T extends Entity> ApplicationModel addOnce(@NonNull EntityModel<T> entityModel) {
        if (entities.get(entityModel.getId()) != null) {
            return this;
        }
        entityModel.setApplicationModel(this);
        entities.put(entityModel.getId(), entityModel);
        return this;
    }

    public Set<String> getEntityIds() {
        return entities.keySet();
    }

    /**
     * returns the entity model for the given entity name, if existent.
     */
    public EntityModel<? extends Entity> getEntity(String entityId) {// NOSONAR
        return entities.get(entityId);
    }

    public Collection<EntityModel<? extends Entity>> getEntityValues() {// NOSONAR
        return entities.values();
    }

    public List<EntityModel<? extends Entity>> getRootEntities() {// NOSONAR
        return entities.values().stream().filter(e -> e.isAggregate()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append(": ");
        sb.append(name).append("\n");
        entitiesToString(sb);
        return sb.toString();
    }

    protected void entitiesToString(StringBuilder sb) {
        if (entities.isEmpty()) {
            return;
        }
        sb.append("Entities: \n");
        entities.keySet().stream()
            .forEach(key -> sb.append(" * ").append(entities.get(key).toString(3)).append("\n"));
    }

}
