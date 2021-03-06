package io.skysail.server.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.restlet.resource.Resource;

import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Nameable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@ToString
@Getter
@Slf4j
public class TreeStructure {

    private String name;
    private String headline;
    private String link = "/";
    private String glyph;
    private List<TreeStructure> subfolders = new ArrayList<>();

    public TreeStructure(@NonNull Nameable nameable, Resource resource, String link, String glyph) {
        this.name = nameable.getName();
        this.headline = nameable.getClass().getSimpleName();
        this.glyph = glyph;
        this.link = "/" ;
        List<Field> collectionsFields = getFieldsOfTypeCollection(nameable);

        if (!collectionsFields.isEmpty()) {
            this.link = link + "/" + nameable.getId().replace("#", "") + "/";
            this.link += collectionsFields.get(0).getName();
        }
        
        collectionsFields.stream().forEach(collectionField -> {
            try {
                handleCollectionField(nameable, resource, glyph, collectionField);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    public static List<TreeStructure> from(@NotNull SkysailServerResource<?> resource) {
    	return resource.getTreeRepresentation();
    }
    
    private void handleCollectionField(Nameable nameable, Resource resource, String glyph, Field collectionField)
            throws IllegalAccessException {
        collectionField.setAccessible(true);
        Collection<?> collection = (Collection<?>) collectionField.get(nameable);
        List<Nameable> subs = collection.stream()
                .filter(e -> e instanceof Nameable)
                .map(Nameable.class::cast).collect(Collectors.toList());
        subs.stream().forEach(subFolder -> 
            addFolder(new TreeStructure(subFolder, resource, this.link, "list-alt".equals(glyph) ? "chevron-right" : "list-alt"))
        );
    }

    private List<Field> getFieldsOfTypeCollection(Nameable nameable) {
        return Arrays.stream(nameable.getClass().getDeclaredFields())
            .filter(field -> Collection.class.isAssignableFrom(field.getType()))
            .collect(Collectors.toList());
    }

    private void addFolder(TreeStructure treeRepresentation) {
        subfolders.add(treeRepresentation);
    }

}
