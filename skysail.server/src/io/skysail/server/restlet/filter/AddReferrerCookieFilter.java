package io.skysail.server.restlet.filter;

import org.restlet.data.CookieSetting;

import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.Constants;
import io.skysail.server.restlet.response.Wrapper;

public class AddReferrerCookieFilter<R extends SkysailServerResource<T>, T extends Entity> extends
        AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, Wrapper<T> responseWrapper) {
        CookieSetting referrerCookie = createCookie();
        if (resource.getReferrerRef() != null) {
            referrerCookie.setValue(resource.getReferrerRef().getPath());
            responseWrapper.getResponse().getCookieSettings().add(referrerCookie);
        }
    }

    private CookieSetting createCookie() {
        CookieSetting cookieSetting = new CookieSetting(Constants.COOKIE_NAME_REFERRER, null);
        cookieSetting.setAccessRestricted(true);
        cookieSetting.setPath("/");
        cookieSetting.setComment("cookie to remember where to redirect to after posts or puts");
        cookieSetting.setMaxAge(300);
        return cookieSetting;
    }
}
