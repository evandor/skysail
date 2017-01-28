package io.skysail.server.restlet.filter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.representation.Variant;

import io.skysail.domain.Entity;
import io.skysail.server.restlet.resources.PutEntityServerResource;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.restlet.response.Wrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PutRedirectGetFilter<R extends PutEntityServerResource<T>, T extends Entity> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, Wrapper<T> responseWrapper) {
        String redirectTo = resource.redirectTo();
        Parameter noRedirects = resource.getQuery() == null ? null : resource.getQuery().getFirst(SkysailServerResource.NO_REDIRECTS);
        if (redirectTo != null && noRedirects == null) {
            Variant variant = (Variant) resource.getRequest().getAttributes().get(SkysailServerResource.SKYSAIL_SERVER_RESTLET_VARIANT);
            if (variant != null && MediaType.TEXT_HTML.equals(variant.getMediaType())) {
                redirectTo = augmentWithMessageIds(redirectTo, responseWrapper.getMessageIds());
                resource.getResponse().redirectSeeOther(redirectTo);
                return;
            }
        }

        Response response = responseWrapper.getResponse();
        String redirectFromQuery = resource.getQuery() != null ? resource.getQuery().getFirstValue("_redirectTo")
                : null;
        if (redirectFromQuery != null) {
            try {
                String seeOther = new URL(redirectFromQuery).toExternalForm();
                seeOther = augmentWithMessageIds(seeOther, responseWrapper.getMessageIds());
                response.redirectSeeOther(seeOther);
                return;
            } catch (MalformedURLException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private String augmentWithMessageIds(String redirectTo, List<Long> messageIds) {
        if (messageIds.isEmpty()) {
            return redirectTo;
        }
        String result;
        if (redirectTo.contains("?")) {
            result = redirectTo + "&";
        } else {
            result = redirectTo + "?";
        }
        return result + "msgIds=" + messageIds.stream().map(id -> id.toString()).collect(Collectors.joining("|"));
    }
}
