package io.skysail.domain.core;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.skysail.domain.Identifiable;
import lombok.Getter;
import lombok.NonNull;

/**
 * This is the root class of skysail's core domain, describing an application,
 * which aggregates valueobjects and entities keeping track of repositories
 * to persist those entities.
 *
 * According to specific needs, the core domain can be adapted by extending the
 * corresponding classes. For example, there's a domain extension dealing with
 * the creation of java source files and classes according to a specific core
 * domain model.
 *
 */
public class ApplicationModel {

    @Getter
    /** an identifier, could be a full qualified class name. */
    private final String name;

    /** the applications entities in a map with their name as key. */
    private final Map<String, EntityModel<? extends Identifiable>> entities = new LinkedHashMap<>();

    /** the applications entities in a map with their name as key. */
    @Getter
    private final Map<String, ValueObjectModel> valueobjects = new LinkedHashMap<>();

    /**
     * an applications unique name; could be a full qualified java identifier.
     */
    public ApplicationModel(String fullQualifiedClassName) {
        this.name = fullQualifiedClassName;
    }

    /**
     * adds an non-null entity model identified by its name.
     *
     * If an entity model with the same name exists already, a debug message is
     * issued and the entity model will not be added again.
     *
     * Otherwise, the entity will be added and the current application will be set
     * in the entity.
     */
    public <T extends Identifiable> ApplicationModel addOnce(@NonNull EntityModel<T> entityModel) {
        if (entities.get(entityModel.getId()) != null) {
            return this;
        }
        entityModel.setApplicationModel(this);
        entities.put(entityModel.getId(), entityModel);
        return this;
    }

	/**
     * @return all entities ids.
     */
    public Set<String> getEntityIds() {
        return entities.keySet();
    }

    /**
     * returns the entity model for the given entity name, if existent.
     */
    public EntityModel<? extends Identifiable> getEntity(String entityId) {// NOSONAR
        return entities.get(entityId);
    }

    public Collection<EntityModel<? extends Identifiable>> getEntityValues() {// NOSONAR
        return entities.values();
    }

    public List<EntityModel<? extends Identifiable>> getRootEntities() {// NOSONAR
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
        entities.keySet().stream().forEach(key -> sb.append(" * ").append(entities.get(key).toString(3)).append("\n"));
    }

}
