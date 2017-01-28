package io.skysail.domain.core;

import io.skysail.domain.Entity;
import io.skysail.domain.html.InputType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Part of skysail's core domain: A FieldModel belongs to an entityModel which
 * belongs to 0..n applicationModels.
 * 
 * This Model is supposed to be extended to provide additional functionality in a
 * more detailed domain.
 * 
 * The FieldModel's class attribute defines the (java) type which is referenced by the field.
 * The FieldModel's inputType is a hint of how the fieldModel should be rendered.
 *
 */
@Getter
@EqualsAndHashCode(of = {"id"})
public class FieldModel {

    /** 
     * the fields unique identifier, e.g. "io.skysail.someapp.someentity.title". 
     */
    private final String id;

    /** the fields (java) type, e.g. java.lang.String */
    private final Class<?> type;

    /** text, textarea, radio, checkbox etc... */
    @Setter
    protected InputType inputType;

    /** a mandatory field must not be null or empty */
    @Setter
    private boolean mandatory;

    /** the field's value cannot be changed */
    @Setter
    private boolean readonly;

    /**
     * if set for a FieldModel of type String, indicates that the rendered value
     * should be truncated
     */
    @Setter
    private Integer truncateTo;

    /**
     * @param entityModel
     * @param fieldId
     * @param cls
     */
    public FieldModel(EntityModel<? extends Entity> entityModel, String fieldId, Class<?> cls) {
        this.id = entityModel.getId() + "|" + fieldId;
        this.type = cls;
        entityModel.add(this);
    }

    public String getInputType() {
        return inputType != null ? inputType.name() : "";
    }

    public String getTag() {
        return inputType != null ? inputType.getTag() : "span";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append("(");
        sb.append("id=").append(getId()).append(", ");
        sb.append("type=").append(type != null ? type.getSimpleName() : "null").append(", ");
        sb.append("inputType=").append(inputType);
        sb.append(")");
        return sb.toString();
    }

}
