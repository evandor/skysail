package io.skysail.server.forms;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PutView {

    Visibility visibility() default Visibility.HIDE;

    /**
     * A string identifier used to organize fields in tabs.
     *
     * @return an identifier
     */
    String tab() default "";
}
