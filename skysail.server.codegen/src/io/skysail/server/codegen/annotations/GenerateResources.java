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

    Class<? extends io.skysail.server.app.SkysailApplication> application();

    ResourceType[] exclude() default {};
}
