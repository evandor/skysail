package io.skysail.domain.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.resources.EntityResource;
import io.skysail.domain.core.resources.ListResource;
import io.skysail.domain.core.resources.PostResource;
import io.skysail.domain.core.resources.PutResource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * A central class of skysail's core domain: An entity belongs to exactly one application
 * and aggregates {@link FieldModel}s describing the entities state along with a couple of
 * resource classes dealing with creation, alteration, reading and deleting this state.
 *
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntityModel<T extends Identifiable> {

    /** ID should be the full qualified java class name, i.e. io.skysail.entity.Customer */
    private String id;

    @Setter
    /** the entities fields in a map with their id as key. */
    private Map<String, FieldModel> fields = new LinkedHashMap<>();
    
    @Setter
    /** names of related entities. */
    private List<EntityRelation> relations = new ArrayList<>();

    @Setter
    /** should this entity be treated as "Aggregate" (DDD)" */
    private boolean aggregate = true;

    @Setter
    /** should be set once an entity model is added to an application. */
    private ApplicationModel applicationModel;
    
//    private ListResource<T> associatedListResource;
//    private EntityResource<T> associatedEntityResource;
//    private PutResource<T> associatedPutResource;
//    private PostResource<T> associatedPostResource;

    public EntityModel(@NonNull String fullQualifiedClassName) {
        this.id = fullQualifiedClassName;
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

    public String getPackageName() {
        int indexOfLastDot = id.lastIndexOf(".");
        if (indexOfLastDot < 0) {
            return "";
        }
        return id.substring(0,indexOfLastDot);
    }
    
    public String getSimpleName() {
        int indexOfLastDot = id.lastIndexOf(".");
        if (indexOfLastDot < 0) {
            return id;
        }
        return id.substring(indexOfLastDot+1);
    }
    
    public EntityModel<?> getAggregateRoot() {
        if (isAggregate()) {
            return this;
        }
        if (getApplicationModel() != null) {
            
            Optional<EntityModel<? extends Identifiable>> parentEntityModel = getApplicationModel().getEntityValues().stream().filter(entity -> 
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

    public String toString(int indentation) {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append(": ");
        sb.append("id='").append(id).append("', isAggregate=").append(isAggregate()).append("\n");
        //resourcesToString(sb);
        fieldsToString(sb);
        relationsToString(sb);
        return sb.toString();
    }
    
    private void relationsToString(StringBuilder sb) {
        if (relations.isEmpty()) {
            return;
        }
        sb.append(StringUtils.repeat(" ", 3)).append("Relations:\n");
        relations.stream().forEach(
                relation -> sb.append(StringUtils.repeat(" ", 3)).append(" - ").append(relation.toString()).append("\n")
        );
    }

    private void fieldsToString(StringBuilder sb) {
        if (fields.isEmpty()) {
            return;
        }
        sb.append(StringUtils.repeat(" ", 3)).append("Fields:\n");
        fields.keySet().stream().forEach(
                key -> sb.append(StringUtils.repeat(" ", 3)).append(" - ").append(fields.get(key).toString()).append("\n")
        );
    }

//    private void resourcesToString(StringBuilder sb) {
//        if (fields.isEmpty()) {
//            return;
//        }
//        if (getAssociatedListResource() != null) {
//            sb.append(StringUtils.repeat(" ", 3)).append("List Resource: ").append(getAssociatedListResource().getResourceClass().getName()).append("\n");
//		}
//		if (getAssociatedEntityResource() != null) {
//            sb.append(StringUtils.repeat(" ", 3)).append("Entity Resource: ").append(getAssociatedEntityResource().getResourceClass().getName()).append("\n");
//		}
//		if (getAssociatedPutResource() != null) {
//            sb.append(StringUtils.repeat(" ", 3)).append("Put Resource: ").append(getAssociatedPutResource().getResourceClass().getName()).append("\n");
//		}
//		if (getAssociatedPostResource() != null) {
//            sb.append(StringUtils.repeat(" ", 3)).append("Post Resource: ").append(getAssociatedPostResource().getResourceClass().getName()).append("\n");
//		}
//    }
//
//	public void addAssociatedResources(EntityModel<? extends Identifiable> otherEntityModel) {
//		if (otherEntityModel.getAssociatedListResource() != null) {
//			setAssociatedListResource(otherEntityModel.getAssociatedListResource().getResourceClass());
//		}
//		if (otherEntityModel.getAssociatedEntityResource() != null) {
//			setAssociatedEntityResource(otherEntityModel.getAssociatedEntityResource().getResourceClass());
//		}
//		if (otherEntityModel.getAssociatedPutResource() != null) {
//			setAssociatedPutResource(otherEntityModel.getAssociatedPutResource().getResourceClass());
//		}
//		if (otherEntityModel.getAssociatedPostResource() != null) {
//			setAssociatedPostResource(otherEntityModel.getAssociatedPostResource().getResourceClass());
//		}
//		
//	}


}
