package io.skysail.server.restlet.filter;

import java.text.ParseException;

import org.restlet.Response;

import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.restlet.response.Wrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FormDataExtractingFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        Response response = responseWrapper.getResponse();
        if (response.getRequest() == null || response.getRequest().getResourceRef() == null) {
            log.warn("request or resourceRef was null");
            return FilterResult.STOP;
        }
        Object data;
        try {
            data = getDataFromRequest(response.getRequest(), resource);
            responseWrapper.setEntity(data);
        } catch (ParseException e) {
            throw new RuntimeException("could not parse form", e);
        }

        return super.doHandle(resource, responseWrapper);
    }

}
