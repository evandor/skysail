package io.skysail.core.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.restlet.resource.ServerResource;

import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.domain.core.EntityModel;
import io.skysail.domain.core.EntityRelation;
import io.skysail.domain.core.EntityRelationType;
import io.skysail.server.domain.jvm.ResourceClass;
import io.skysail.server.domain.jvm.ResourceType;
import io.skysail.server.facets.FacetsProvider;
import io.skysail.server.forms.Tab;
import io.skysail.server.utils.MyCollectors;
import io.skysail.server.utils.ReflectionUtils;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString(callSuper = true)
public class SkysailEntityModel<T extends Entity> extends EntityModel<T> {

    @Getter
    protected Class<T> identifiableClass;

    private Map<ResourceType, ResourceClass> associatedResources = new EnumMap<>(ResourceType.class);

    private volatile Set<Tab> tabs;

    public SkysailEntityModel(FacetsProvider facetsProvider, Class<T> identifiableClass, SkysailServerResource<?> resourceInstance) {
        super(identifiableClass.getName());
        this.identifiableClass = identifiableClass;
        deriveFields(facetsProvider, identifiableClass);
        deriveRelations(identifiableClass);
        setAssociatedResourceClass(resourceInstance);
    }

    public Class<? extends ServerResource> getPostResourceClass() {
        if (identifiableClass.getPackage() == null) {
            return getClass(
                    packageOf(identifiableClass.getName()) + ".Post" + identifiableClass.getSimpleName() + "Resource");
        }
        return getClass(
                identifiableClass.getPackage().getName() + ".Post" + identifiableClass.getSimpleName() + "Resource");
    }

    public Class<? extends ServerResource> getPutResourceClass() {
        if (identifiableClass.getPackage() == null) {
            return getClass(
                    packageOf(identifiableClass.getName()) + ".Put" + identifiableClass.getSimpleName() + "Resource");
        }
        return getClass(
                identifiableClass.getPackage().getName() + ".Put" + identifiableClass.getSimpleName() + "Resource");
    }

    @Deprecated // Use associatedResources get ResourceType.LIST
    public Class<? extends ServerResource> getListResourceClass() {
        if (identifiableClass.getPackage() == null) {
            return getClass(identifiableClass.getName() + "sResource");
        }
        return getClass(
                identifiableClass.getPackage().getName() + "." + identifiableClass.getSimpleName() + "sResource");
    }

    public Class<? extends ServerResource> getEntityResourceClass() {
        if (identifiableClass.getPackage() == null) {
            return getClass(identifiableClass.getName() + "Resource");
        }
        return getClass(
                identifiableClass.getPackage().getName() + "." + identifiableClass.getSimpleName() + "Resource");
    }

    public ResourceClass getAssociatedResource(ResourceType type) {
        ResourceClass associatedResource = associatedResources.get(type);
        if (associatedResource == null || associatedResource.getResourceClass() == null) {
            SkysailApplicationModel appModel = (SkysailApplicationModel) getApplicationModel();
            SkysailEntityModel<?> superTypeEntity = (SkysailEntityModel<?>) appModel.getEntitySupertype(identifiableClass.getName());
            associatedResource = superTypeEntity != null ? superTypeEntity.getAssociatedResource(type) : null;

            if (associatedResource == null) {
                SkysailEntityModel<?> superSubEntity = (SkysailEntityModel<?>) appModel.getEntitySubtype(identifiableClass.getName());
                associatedResource = superSubEntity.getAssociatedResource(type);
            }
        }
        return associatedResource;
    }


    @Override
    public String toString(int indentation) {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append(": ");
        sb.append("name='").append(getName()).append("', isAggregate=").append(isAggregate()).append("\n");
        associatedResourcesToString(sb);
        fieldsToString(sb);
        relationsToString(sb);
        fieldRelationsToString(sb);

        return sb.toString();
    }

    private void associatedResourcesToString(StringBuilder sb) {
        associatedResources.entrySet().stream().forEach(type -> {
            sb.append("   associated Resource: ").append(type.getKey().name()).append(" -> ").append(type.getValue().getResourceClass()).append("\n");
        });
    }


    @SuppressWarnings("unchecked")
    private Class<? extends ServerResource> getClass(String classname) {
        try {
            log.debug("searching for class '{}'", classname);
            return (Class<? extends ServerResource>) Class.forName(classname, false,
                    identifiableClass.getClassLoader());
        } catch (ClassNotFoundException e) {
            log.info(e.getMessage());
        }
        return null;
    }

    private void deriveFields(FacetsProvider facetsProvider, Class<? extends Entity> cls) {
        setFields(ReflectionUtils.getInheritedFields(cls).stream()
                    .filter(this::filterFormFields)
                    .map(f -> new SkysailFieldModel(facetsProvider, this, f))
                .   collect(MyCollectors.toLinkedMap(SkysailFieldModel::getId, Function.identity())));
    }

//    private void deriveFieldRelations(SkysailApplication skysailApplication, Class<? extends Entity> cls) {
//        setFieldRelations(ReflectionUtils.getInheritedFields(cls).stream().filter(this::filterFieldRelations)
//                .map(f -> new SkysailFieldRelationModel(skysailApplication, f))
//                .collect(MyCollectors.toLinkedMap(SkysailFieldRelationModel::getId, Function.identity())));
//    }


    private boolean filterFormFields(Field f) {
        return f.getAnnotation(io.skysail.domain.html.Field.class) != null;
    }

    private String packageOf(String fullQualifiedName) {
        String[] split = fullQualifiedName.split("\\.");
        return Arrays.stream(Arrays.copyOf(split, split.length - 1)).collect(Collectors.joining("."));
    }

    private void deriveRelations(Class<? extends Entity> cls) {
        setRelations(ReflectionUtils.getInheritedFields(cls).stream().filter(this::filterRelationFields)
                .map(Field::getName)
                .map(r -> new EntityRelation(r, null, EntityRelationType.ONE_TO_MANY))
                .collect(Collectors.toList()));
    }

    private void setAssociatedResourceClass(SkysailServerResource<?> resourceClass) {
        if (resourceClass == null) {
            return;
        }
        associatedResources.put(resourceClass.getResourceType(), new ResourceClass(resourceClass));

    }

    private boolean filterRelationFields(Field f) {
        return f.getAnnotation(io.skysail.domain.html.Relation.class) != null;
    }

//    private boolean filterFieldRelations(Field f) {
//        return f.getAnnotation(io.skysail.domain.html.FieldRelation.class) != null;
//    }

    public synchronized Set<Tab> getTabs() {
        if (tabs != null) {
            return tabs;
        }
        List<String> tabNamesSet = getFieldValues().stream()
        		.map(SkysailFieldModel.class::cast)
        		.map(SkysailFieldModel::getPostTabName)
                .filter(name -> name != null)
                .collect(Collectors.toList());

        if (tabNamesSet.isEmpty() || tabNamesSet.size() == 1) {
            return Collections.emptySet();
        }

        tabs = new LinkedHashSet<>();
        for (String aTab : tabNamesSet) {
            tabs.add(new Tab(aTab, aTab, tabs.size()));
        }

        return tabs;
    }

}
