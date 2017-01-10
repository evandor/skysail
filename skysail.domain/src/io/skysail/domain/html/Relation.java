package io.skysail.domain.html;

import java.lang.annotation.*;

/**
 * annotate entities' fields with this annotation to indicate that the field
 * references another entity (in the same application)
 *
 * TODO rename or unite with FieldRelation
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Relation {

}
