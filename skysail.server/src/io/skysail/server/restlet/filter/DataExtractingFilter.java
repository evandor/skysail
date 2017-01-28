package io.skysail.server.restlet.filter;

import java.lang.reflect.Field;
import java.util.List;

import io.skysail.domain.Entity;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.restlet.response.Wrapper;
import io.skysail.server.utils.CookiesUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataExtractingFilter<R extends SkysailServerResource<?>, T extends Entity>
        extends AbstractResourceFilter<R, T> {

    @SuppressWarnings("unchecked")
    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());

        String installation = CookiesUtils.getInstallationFromCookie(resource.getRequest()).orElse(null);
        Object entity = resource.getEntity(installation);
        if (entity instanceof List) {
            List<T> data = (List<T>) entity;
            sanitizeIds(data);

            responseWrapper.setEntity(data);
            resource.setCurrentEntity(data); // TODO why both wrapper AND resource?
        } else {
            sanitizeIds((T)entity);

            responseWrapper.setEntity(entity);
            resource.setCurrentEntity(entity); // TODO why both wrapper AND resource?

        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

    private void sanitizeIds(List<T> data) {
        data.stream().forEach(element -> {
            if (element instanceof Entity) {
                replaceHash(element);
            }
        });
    }

    private void sanitizeIds(T data) {
        if (data instanceof List) {
            ((List<?>) data).stream().forEach(element -> {
                if (element instanceof Entity) {
                    replaceHash(element);
                }
            });
        } else if (data instanceof Entity) {
            replaceHash(data);
        }
    }

    private void replaceHash(Object element) {
        Entity identifiable = (Entity) element;
        if (identifiable.getId() != null) {

            try {
                Field field = identifiable.getClass().getDeclaredField("id");
                field.setAccessible(true);
                field.set(identifiable, identifiable.getId().replace("#", ""));
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }

        }
    }

}
