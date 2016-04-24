package io.skysail.server.domain.jvm;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.restlet.resource.ServerResource;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.EntityModel;
import io.skysail.domain.core.EntityRelation;
import io.skysail.domain.core.EntityRelationType;
import io.skysail.domain.core.resources.EntityResource;
import io.skysail.domain.core.resources.ListResource;
import io.skysail.domain.core.resources.PostResource;
import io.skysail.domain.core.resources.PutResource;
import io.skysail.server.forms.Tab;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.restlet.resources.PutEntityServerResource;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.MyCollectors;
import io.skysail.server.utils.ReflectionUtils;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString(callSuper = true)
public class JavaEntityModel<T extends Identifiable> extends EntityModel<T> {

	@Getter
	protected Class<T> identifiableClass;

	@Getter
	private ListResource<T> associatedListResource;
	@Getter
	private EntityResource<T> associatedEntityResource;
	@Getter
	private PutResource<T> associatedPutResource;
	@Getter
	private PostResource<T> associatedPostResource;

	private volatile Set<Tab> tabs;

	public JavaEntityModel(Class<T> identifiableClass, SkysailServerResource<?> resourceInstance) {
		super(identifiableClass.getName());
		this.identifiableClass = identifiableClass;
		deriveFields(identifiableClass);
		deriveRelations(identifiableClass);
		deriveResources(resourceInstance);
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
	
    public String toString(int indentation) {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append(": ");
        sb.append("id='").append(getId()).append("', isAggregate=").append(isAggregate()).append("\n");
        associatedResourcesToString(sb);
        fieldsToString(sb);
        relationsToString(sb);
        return sb.toString();
    }
    
    private void associatedResourcesToString(StringBuilder sb) {
    	sb.append("associated ListResource: ").append(getAssociatedListResource()).append("\n");
    	sb.append("associated EntityResource: ").append(getAssociatedEntityResource()).append("\n");
    	sb.append("associated PutResource: ").append(getAssociatedPutResource()).append("\n");
    	sb.append("associated PostResource: ").append(getAssociatedPostResource()).append("\n");
	}


	@SuppressWarnings("unchecked")
	private Class<? extends ServerResource> getClass(String classname) {
		try {
			log.debug("searching for class '{}'", classname);
			return (Class<? extends ServerResource>) Class.forName(classname, false,
					identifiableClass.getClassLoader());
		} catch (ClassNotFoundException e) {
			log.info(e.getMessage());
			return null;
		}
	}

	private void deriveFields(Class<? extends Identifiable> cls) {
		setFields(ReflectionUtils.getInheritedFields(cls).stream().filter(this::filterFormFields)
				.map(f -> new JavaFieldModel(f))
				.collect(MyCollectors.toLinkedMap(JavaFieldModel::getId, Function.identity())));
	}

	private boolean filterFormFields(Field f) {
		return f.getAnnotation(io.skysail.domain.html.Field.class) != null;
	}

	private String packageOf(String fullQualifiedName) {
		String[] split = fullQualifiedName.split("\\.");
		return Arrays.stream(Arrays.copyOf(split, split.length - 1)).collect(Collectors.joining("."));
	}

	private void deriveRelations(Class<? extends Identifiable> cls) {
		setRelations(ReflectionUtils.getInheritedFields(cls).stream().filter(f -> filterRelationFields(f))
				.map(f -> f.getName()).map(r -> new EntityRelation(r, null, EntityRelationType.ONE_TO_MANY))
				.collect(Collectors.toList()));
	}

	private void deriveResources(SkysailServerResource<?> resourceClass) {
		if (resourceClass == null) {
			return;
		}
		if (resourceClass instanceof ListServerResource) {
			setAssociatedListResource(resourceClass.getClass());
		} else if (resourceClass instanceof EntityServerResource) {
			setAssociatedEntityResource(resourceClass.getClass());
		} else if (resourceClass instanceof PutEntityServerResource) {
			setAssociatedPutResource(resourceClass.getClass());
		} else if (resourceClass instanceof PostEntityServerResource) {
			setAssociatedPostResource(resourceClass.getClass());
		}

	}

	public void setAssociatedListResource(Class<?> resourceClass) {
		this.associatedListResource = new ListResource(resourceClass);
	}

	public void setAssociatedEntityResource(Class<?> resourceClass) {
		this.associatedEntityResource = new EntityResource(resourceClass);
	}

	public void setAssociatedPutResource(Class<?> resourceClass) {
		this.associatedPutResource = new PutResource(resourceClass);
	}

	public void setAssociatedPostResource(Class<?> resourceClass) {
		this.associatedPostResource = new PostResource(resourceClass);
	}

	private boolean filterRelationFields(Field f) {
		return f.getAnnotation(io.skysail.domain.html.Relation.class) != null;
	}

	public synchronized Set<Tab> getTabs() {
		if (tabs != null) {
			return tabs;
		}
		Set<String> tabNamesSet = getFieldValues().stream().map(JavaFieldModel.class::cast).map(f -> f.getPostTabName())
				.filter(name -> name != null).collect(Collectors.toSet());

		if (tabNamesSet.isEmpty() || tabNamesSet.size() == 1) {
			return Collections.emptySet();
		}

		tabs = new HashSet<>();
		int i = 0;
		for (String aTab : tabNamesSet) {
			tabs.add(new Tab(aTab, aTab, i++));
		}

		return tabs;
	}
}
