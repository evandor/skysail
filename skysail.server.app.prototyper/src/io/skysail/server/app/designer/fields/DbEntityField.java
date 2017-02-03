package io.skysail.server.app.designer.fields;

import java.io.Serializable;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.skysail.domain.Entity;
import io.skysail.domain.Nameable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.app.designer.fields.provider.VisibilitySelectionProvider;
import io.skysail.server.app.designer.fields.resources.InputTypeSelectionProvider;
import io.skysail.server.app.designer.fields.roles.FieldRoleSelectionProvider;
import io.skysail.server.forms.PostView;
import io.skysail.server.forms.Visibility;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public abstract class DbEntityField implements Entity, Nameable, Serializable {

    private static final long serialVersionUID = -3876765006276811418L;

    @Id
    @Setter
    protected String id;

    @Field
    @NotNull
    @Size(min = 1)
    @PostView(tab = "newField")
    protected String name;

    @Field(selectionProvider = VisibilitySelectionProvider.class)
    @PostView(tab = "visibility")
    protected String listViewVisibility = Visibility.SHOW.name();

    @Field(selectionProvider = VisibilitySelectionProvider.class)
    @PostView(tab = "visibility")
    protected String postViewVisibility = Visibility.SHOW.name();

    @Field(selectionProvider = VisibilitySelectionProvider.class)
    @PostView(tab = "visibility")
    protected String putViewVisibility = Visibility.SHOW.name();

    @Field(selectionProvider = InputTypeSelectionProvider.class)
    @PostView(visibility = Visibility.HIDE)
    private InputType type;

    @Field(inputType = InputType.CHECKBOX)
    @PostView(tab = "optional")
    protected Boolean mandatory;

    @Field(selectionProvider = FieldRoleSelectionProvider.class)
    @PostView(tab = "special")
    protected FieldRole role = FieldRole.NONE;

    public Class<?> getFieldType() {
        return String.class;
    }


}
