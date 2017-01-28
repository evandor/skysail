package io.skysail.domain.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import io.skysail.domain.Entity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * A central class of skysail's core domain: An entity belongs to exactly one application
 * and aggregates {@link FieldModel}s describing the entities state.
 *
 * An entity is identified by its ID only.
 *
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of="id")
public class EntityModel<T extends Entity> {

    private static final String DOT = ".";

    /** ID should be the full qualified java class name, e.g. io.skysail.entity.Customer */
    private String id;

    /** e.g. Customer */
    private String name;

    @Setter
    /** the entities fields in a map with their id as key. */
    private Map<String, FieldModel> fields = new LinkedHashMap<>();

    @Setter
    /** names of related entities in other applications. */
    private Map<String, FieldRelationModel> fieldRelations = new LinkedHashMap<>();

    @Setter
    /** names of related entities. */
    private List<EntityRelation> relations = new ArrayList<>();

    @Setter
    /** should this entity be treated as "Aggregate" (DDD)" */
    private boolean aggregate = true;

    @Setter
    /** should be set once an entity model is added to an application. */
    private ApplicationModel applicationModel;

    public EntityModel(@NonNull String fullQualifiedClassName) {
        this.id = fullQualifiedClassName;
        if (fullQualifiedClassName.contains(DOT)) {
        	this.name = fullQualifiedClassName.substring(1+fullQualifiedClassName.lastIndexOf(DOT));
        } else {
        	this.name = this.id;
        }
    }

    public EntityModel<T> add(@NonNull FieldModel field) {
        this.fields.put(field.getId(), field);
        return this;
    }

    public Set<String> getFieldNames() {
        return fields.keySet();
    }

    public FieldModel getField(String identifier) {
        return fields.get(identifier);
    }

    public Collection<FieldModel> getFieldValues() {
        return fields.values();
    }

    public Collection<FieldRelationModel> getFieldRelationValues() {
        return fieldRelations.values();
    }

    public String getPackageName() {
        int indexOfLastDot = id.lastIndexOf(DOT);
        if (indexOfLastDot < 0) {
            return "";
        }
        return id.substring(0,indexOfLastDot);
    }

    public String getSimpleName() {
        int indexOfLastDot = id.lastIndexOf(DOT);
        if (indexOfLastDot < 0) {
            return id;
        }
        return id.substring(indexOfLastDot+1);
    }

    @SuppressWarnings("unchecked")
	public EntityModel<Entity> getAggregateRoot() {
        if (isAggregate()) {
            return (EntityModel<Entity>)this;
        }
        if (getApplicationModel() != null) {

            Optional<EntityModel<? extends Entity>> parentEntityModel = getApplicationModel().getEntityValues().stream().filter(entity ->
                entity.getRelations().stream().filter(relation ->
                   relation.getTargetEntityModel().equals(this)
                ).findFirst().isPresent()
            ).findFirst();
            if (parentEntityModel.isPresent()) {
                return parentEntityModel.get().getAggregateRoot();
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return toString(0);
    }

    public String toString(int indentation) { // NOSONAR
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append(": ");
        sb.append("id='").append(id).append("', isAggregate=").append(isAggregate()).append("\n");
        fieldsToString(sb);
        relationsToString(sb);
        return sb.toString();
    }

    protected void relationsToString(StringBuilder sb) {
        if (relations.isEmpty()) {
            return;
        }
        sb.append(repeat(" ", 3)).append("Relations:\n");
        relations.stream().forEach(
                relation -> sb.append(repeat(" ", 3)).append(" - ").append(relation.toString()).append("\n")
        );
    }

    protected void fieldRelationsToString(StringBuilder sb) {
        if (fieldRelations.isEmpty()) {
            return;
        }
        sb.append(repeat(" ", 3)).append("FieldRelations:\n");
        fieldRelations.keySet().stream().forEach(
                key -> sb.append(repeat(" ", 3)).append(" - ").append(fields.get(key).toString()).append("\n")
        );
    }

    protected void fieldsToString(StringBuilder sb) {
        if (fields.isEmpty()) {
            return;
        }
        sb.append(repeat(" ", 3)).append("Fields:\n");
        fields.keySet().stream().forEach(
                key -> sb.append(repeat(" ", 3)).append(" - ").append(fields.get(key).toString()).append("\n")
        );
    }

    private String repeat(String s, int i) {
        StringBuilder str = new StringBuilder();
        IntStream.rangeClosed(1,i).forEach(dummy -> str.append(s)); // NOSONAR
        return str.toString();
    }

}
