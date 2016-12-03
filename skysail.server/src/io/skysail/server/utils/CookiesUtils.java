package io.skysail.server.utils;

import java.util.Optional;

import org.restlet.Request;
import org.restlet.data.Cookie;
import org.restlet.data.CookieSetting;

import io.skysail.server.Constants;
import io.skysail.server.rendering.RenderingMode;

public class CookiesUtils {
    
    public static CookieSetting createCookie(String name, String path, int maxAgeInSeconds) {
        CookieSetting cookieSetting = new CookieSetting(name, null);
        cookieSetting.setAccessRestricted(true);
        cookieSetting.setPath(path);
        //cookieSetting.setComment("cookie to remember where to redirect to after posts or puts");
        cookieSetting.setMaxAge(maxAgeInSeconds);
        return cookieSetting;
    }


    /**
     * cookie is set by navbar.stg 
     */
    public static Optional<String> getThemeFromCookie(Request request) {
        return getOptionalCookieValue(request,Constants.COOKIE_NAME_THEME);
    }

//    public static RenderingMode getModeFromCookie(Request request) {
//        Optional<String> returnCookieOrNull = returnCookieOrNull(request,Constants.COOKIE_NAME_MODE);
//        if (!returnCookieOrNull.isPresent()) {
//            return RenderingMode.DEFAULT;
//        }
//        return RenderingMode.valueOf(returnCookieOrNull.get().toUpperCase());
//   }

    public static RenderingMode getModeFromCookie(Request request) {
    	return RenderingMode.valueOf(getOptionalCookieValue(request,Constants.COOKIE_NAME_MODE).orElse(RenderingMode.DEFAULT.name()).toUpperCase());
    }

    public static Optional<String> getMainPageFromCookie(Request request) {
        return getOptionalCookieValue(request,Constants.COOKIE_NAME_MAINPAGE);
    }

    public static Optional<String> getInstallationFromCookie(Request request) {
        return getOptionalCookieValue(request,Constants.COOKIE_NAME_INSTALLATIONS);
    }

    public static Optional<String> getEntriesPerPageFromCookie(Request request) {
        return getOptionalCookieValue(request,Constants.COOKIE_NAME_ENTRIES_PER_PAGE);
    }

    public static Optional<String> getReferrerFromCookie(Request request) {
        return getOptionalCookieValue(request,Constants.COOKIE_NAME_REFERRER);
    }

    private static Optional<String> getOptionalCookieValue(Request request, String name) {
        if (request == null || request.getCookies() == null) {
            return Optional.empty();
        }
        Cookie templateCookie = request.getCookies().getFirst(name);
        if (templateCookie == null) {
            return Optional.empty();
        }
        return Optional.of(templateCookie.getValue());
    }


    
}
