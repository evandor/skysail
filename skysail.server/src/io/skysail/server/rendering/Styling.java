package io.skysail.server.rendering;

import org.restlet.data.CookieSetting;

import io.skysail.server.Constants;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.CookiesUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString(of = "name")
public class Styling {

	private static final String DEFAULT_STYLING = "";

	@Getter
	private final String name;

	public Styling(String styling) {
		this.name = styling;
	}

	public static Styling determineForm(@NonNull SkysailServerResource<?> resource) {
		String stylingFromRequest = resource.getQuery() != null ? resource.getQuery().getFirstValue("_styling") : null;
		if (stylingFromRequest == null) {
			return new Styling(CookiesUtils.getStylingFromCookie(resource.getRequest()).orElse(DEFAULT_STYLING));

		}
		Styling styling = new Styling(stylingFromRequest);
		CookieSetting stylingCookie = CookiesUtils.createCookie(Constants.COOKIE_NAME_STYLING,
				resource.getRequest().getResourceRef().getPath(), -1);
		stylingCookie.setValue(stylingFromRequest);
		resource.getResponse().getCookieSettings().add(stylingCookie);
		return styling;
	}

}
