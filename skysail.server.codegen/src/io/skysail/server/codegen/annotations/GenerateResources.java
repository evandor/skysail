package io.skysail.server.codegen.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.skysail.server.codegen.ResourceType;


@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenerateResources {

    /**
     * If Type "class" was being used here, we'd have to add all skysail.server dependencies to codegen,
     * which we do not want.
     *
     * The string should be the full qualified name of a SkysailApplication.
     */
    String application() default "TheApplicationExtendingSkysailApplication";

    ResourceType[] exclude() default {};
}
