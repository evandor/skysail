package io.skysail.server.forms;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.skysail.api.links.LinkRelation;
import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ListView {

    public static final class DEFAULT extends SkysailServerResource<Identifiable> {

        @Override
        public Identifiable getEntity() {
            return null;
        }

        @Override
        public LinkRelation getLinkRelation() {
            return null;
        }

    }

    Class<? extends SkysailServerResource<?>> link() default DEFAULT.class;

    int truncate() default -1;

    boolean hide() default false;

    String colorize() default "";

    String prefix() default "";
}
