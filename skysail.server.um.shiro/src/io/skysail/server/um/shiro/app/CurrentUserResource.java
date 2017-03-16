package io.skysail.server.um.shiro.app;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.um.domain.SkysailUser;


public class CurrentUserResource extends EntityServerResource<SkysailUser> {

    public CurrentUserResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Show current user");
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public SkysailUser getEntity() {
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        return new SkysailUser(principal == null ? null : principal.toString(), null, null);
    }


}
