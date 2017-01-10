package io.skysail.server.model;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import io.skysail.domain.html.FieldRelation;
import io.skysail.domain.html.Reference;
import io.skysail.server.domain.jvm.SkysailApplicationService;
import io.skysail.server.forms.FormField;
import io.skysail.server.forms.ListView;
import io.skysail.server.forms.PostView;
import io.skysail.server.forms.PutView;
import io.skysail.server.forms.Visibility;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.restlet.resources.PutEntityServerResource;
import io.skysail.server.restlet.resources.SkysailServerResource;

public abstract class FieldFactory {

    public abstract Map<String, FormField> determineFrom(SkysailServerResource<?> resource, SkysailApplicationService appService);

    protected boolean test(SkysailServerResource<?> resource, List<String> fieldNames, Field field) {
        if (resource == null) {
            return true;
        }
        //List<String> fieldNames = resource.getFields();
        if (isValidFieldAnnotation(resource, field, fieldNames)) {
            return true;
        }
        return false;
    }

    private boolean isValidFieldAnnotation(SkysailServerResource<?> resource, Field field, List<String> fieldNames) {
        io.skysail.domain.html.Field fieldAnnotation = field.getAnnotation(io.skysail.domain.html.Field.class);
        Reference referenceAnnotation = field.getAnnotation(Reference.class);
        FieldRelation fieldRelation = field.getAnnotation(FieldRelation.class);
        if (fieldAnnotation == null && referenceAnnotation == null && fieldRelation == null) {
            return false;
        }
        if (!(fieldNames.contains(field.getName()))) {
            if (!subFieldExistsFor(fieldNames, field.getName())) {
                return false;
            }
        }
        if (resource instanceof PostEntityServerResource<?>) {
            return isValid(field, resource);
        }
        if (resource instanceof PutEntityServerResource<?>) {
            return isValid(field, (PutEntityServerResource<?>)resource);
        }
        if (resource instanceof ListServerResource<?>) {
            return isValid(field, (ListServerResource<?>)resource);
        }

        return true;
    }

    private boolean subFieldExistsFor(List<String> fieldNames, String name) {
        return fieldNames.stream().filter(f -> f.startsWith(name + ".")).findFirst().isPresent();
    }

    private boolean isValid(Field field, ListServerResource<?> resource) {
        ListView listViewAnnotation = field.getAnnotation(ListView.class);
        if (listViewAnnotation == null) {
            return true;
        }
        return !listViewAnnotation.hide();
    }

    private boolean isValid(Field field, SkysailServerResource<?> resource) {
        PostView postViewAnnotation = field.getAnnotation(PostView.class);
        if (postViewAnnotation != null) {
            if (Visibility.HIDE.equals((postViewAnnotation.visibility()))) {
                return false;
            }
            if (Visibility.SHOW.equals(postViewAnnotation.visibility())) {
                return true;
            }
            if (Visibility.SHOW_IF_NULL.equals(postViewAnnotation.visibility())) {
                if (!resource.getRequest().toString().contains("/" + field.getName() + ":null/")) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(Field field, PutEntityServerResource<?> resource) {
        PutView putViewAnnotation = field.getAnnotation(PutView.class);
        if (putViewAnnotation != null) {
            if (Visibility.HIDE.equals((putViewAnnotation.visibility()))) {
                return false;
            }
            if (Visibility.SHOW.equals(putViewAnnotation.visibility())) {
                return true;
            }
            if (Visibility.SHOW_IF_NULL.equals(putViewAnnotation.visibility())) {
                if (resource.getRequest().toString().contains("/" + field.getName() + ":null/")) {
                    return true;
                }
            }
        }
        return true;
    }

}
