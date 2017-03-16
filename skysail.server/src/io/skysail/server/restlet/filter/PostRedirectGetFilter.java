package io.skysail.server.restlet.filter;

import java.util.List;
import java.util.stream.Collectors;

import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.representation.Variant;

import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.response.Wrapper;

public class PostRedirectGetFilter<R extends SkysailServerResource<T>, T extends Entity> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, Wrapper<T> responseWrapper) {
    	if (resource.getQuery() == null) {
    		return;
    	}
        String redirectTo = resource.redirectTo();
        Parameter noRedirects = resource.getQuery().getFirst(SkysailServerResource.NO_REDIRECTS);
        if (redirectTo != null && noRedirects == null) {
            Variant variant = (Variant) resource.getRequest().getAttributes().get(SkysailServerResource.SKYSAIL_SERVER_RESTLET_VARIANT);
            if (MediaType.TEXT_HTML.equals(variant.getMediaType())) {
                redirectTo = augmentWithMessageIds(redirectTo, responseWrapper.getMessageIds());
                resource.getResponse().redirectSeeOther(redirectTo);
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
