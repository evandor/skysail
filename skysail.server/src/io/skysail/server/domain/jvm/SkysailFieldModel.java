package io.skysail.server.domain.jvm;

import java.lang.reflect.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.skysail.domain.html.InputType;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.facets.FacetsProvider;
import io.skysail.server.forms.ListView;
import io.skysail.server.forms.PostView;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class SkysailFieldModel extends io.skysail.domain.core.FieldModel {

    @Getter
    private Class<? extends SkysailServerResource<?>> listViewLink;

    @Getter
    private String format;

    private Field f;

    @Getter
    private FieldFacet facet;

    public SkysailFieldModel(SkysailApplication skysailApplication, java.lang.reflect.Field f) {
        super(f.getName(), String.class);
        this.f = f;
        setInputType(determineInputType(f));
        setMandatory(determineIfMandatory(f));
        setReadonly(false);
        setTruncateTo(determineTruncation(f));
        setType(f.getType());

        listViewLink = determineListViewLink(f);
        format = determineFormat(f);
        facet = determineFacet(skysailApplication,f);
    }

    public String getPostTabName() {
        PostView postAnnotation = f.getAnnotation(PostView.class);
        return postAnnotation == null ? null : postAnnotation.tab();
    }

    private Class<? extends SkysailServerResource<?>> determineListViewLink(Field f) {
        ListView listViewAnnotation = f.getAnnotation(ListView.class);
        if (listViewAnnotation != null && !listViewAnnotation.link().equals(ListView.DEFAULT.class)) {
            return listViewAnnotation.link();
        }
        return null;
    }

    private String determineFormat(Field f) {
        ListView annotation = f.getAnnotation(ListView.class);
        if (annotation != null && !"".equals(annotation.format())) {
            return annotation.format();
        }
        return null;
    }

    private FieldFacet determineFacet(SkysailApplication skysailApplication, Field f) {
    	FacetsProvider facetsProvider = skysailApplication.getFacetsProvider();
    	return facetsProvider == null ? null : facetsProvider.getFacetFor(f.getDeclaringClass().getName() + "." + f.getName());
    }



    private InputType determineInputType(Field f) {
        return f.getAnnotation(io.skysail.domain.html.Field.class).inputType();
    }

    private boolean determineIfMandatory(Field f) {
        NotNull notNullAnnotation = f.getAnnotation(NotNull.class);
        if (notNullAnnotation != null) {
            return true;
        }
        Size sizeAnnotation = f.getAnnotation(Size.class);
        if (sizeAnnotation != null) {
            int min = sizeAnnotation.min();
            if (min > 0) {
                return true;
            }
        }
        return false;
    }

    private Integer determineTruncation(Field f) {
        ListView listViewAnnotation = f.getAnnotation(ListView.class);
        if (listViewAnnotation != null && !listViewAnnotation.link().equals(ListView.DEFAULT.class)) {
            return null;
        }
        return null;
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append("(");
        sb.append("name=").append(getName()).append(", ");
        sb.append("type=").append(type != null ? type.getSimpleName() : "null").append(", ");
        sb.append("inputType=").append(inputType);
        if (facet != null) {
        	sb.append(", ");
            sb.append("facet=").append(facet);
        }
        sb.append(")");
        return sb.toString();
    }

}
