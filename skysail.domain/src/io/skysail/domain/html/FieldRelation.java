package io.skysail.domain.html;

import java.lang.annotation.*;

import io.skysail.domain.Identifiable;

/**
 * annotate entities' fields with this annotation to indicate that the field
 * references another entity (in another application)
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldRelation {
    
    Class<? extends Identifiable> targetEntity();

}
