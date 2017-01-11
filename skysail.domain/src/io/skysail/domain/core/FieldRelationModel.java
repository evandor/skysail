package io.skysail.domain.core;

import io.skysail.domain.html.InputType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Part of skysail's core domain: A FieldModel belongs to an entity which
 * belongs to an application.
 *
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class FieldRelationModel {

    private static final String DOT = ".";

    /** the fields name or identifier, e.g. "title" */
    private final String id;

    private final String name;

    /** a mandatory field must not be null or empty */
    private boolean mandatory;

    /** the field cannot be changed */
    private boolean readonly;

    /** the fields (java) type, e.g. java.lang.String */
    protected Class<?> type = String.class;

    /** text, textarea, radio, checkbox etc... */
    protected InputType inputType;

    /**
     * if set for a FieldModel of type String, indicates that the rendered value
     * should be truncated
     */
    private Integer truncateTo;

    public FieldRelationModel(String id, Class<?> cls) {
        this.id = id;
        this.type = cls;
        if (id.contains(DOT)) {
        	this.name = id.substring(1+id.lastIndexOf(DOT));
        } else {
        	this.name = this.id;
        }
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
        sb.append("name=").append(name).append(", ");
        sb.append("type=").append(type != null ? type.getSimpleName() : "null").append(", ");
        sb.append("inputType=").append(inputType);
        sb.append(")");
        return sb.toString();
    }

}
