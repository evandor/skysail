package io.skysail.server.rendering;

import java.util.Optional;

import org.restlet.data.CookieSetting;

import io.skysail.server.Constants;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.services.StringTemplateProvider;
import io.skysail.server.utils.CookiesUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString(of = {"name","selected"})
public class Styling implements Comparable<Styling> {

	private static final String DEFAULT_STYLING = "";

	@Getter
	private final String name;

	@Getter
	private final String label;

	@Getter
	private final boolean selected;

	public Styling(@NonNull String styling, boolean selected) {
		this.name = styling;
		this.label = firstUppercaseOf(styling);
		this.selected = selected;
	}

	public static Styling determineForm(@NonNull SkysailServerResource<?> resource) {
		String stylingFromRequest = resource.getQuery() != null ? resource.getQuery().getFirstValue("_styling") : null;
		if (stylingFromRequest == null) {
			return new Styling(CookiesUtils.getStylingFromCookie(resource.getRequest()).orElse(DEFAULT_STYLING), false);

		}
		Styling styling = new Styling(stylingFromRequest, true);
		CookieSetting stylingCookie = CookiesUtils.createCookie(Constants.COOKIE_NAME_STYLING,
				resource.getRequest().getResourceRef().getPath(), -1);
		stylingCookie.setValue(stylingFromRequest);
		resource.getResponse().getCookieSettings().add(stylingCookie);
		return styling;
	}

	public static Styling checkSelected(String namespace, @NonNull SkysailServerResource<?> resource) {
		String stylingFromRequest = resource.getQuery() != null ? resource.getQuery().getFirstValue("_styling") : null;
		if (stylingFromRequest != null && stylingFromRequest.equals(namespace)) {
			return new Styling(stylingFromRequest, true);
		}
		Optional<String> stylingFromCookie = CookiesUtils.getStylingFromCookie(resource.getRequest());
		return new Styling(namespace, stylingFromCookie.isPresent() && stylingFromCookie.get().equals(namespace));
	}

	@Override
	public int compareTo(Styling o) {
		return name.compareTo(o.getName());
	}
	
	private String firstUppercaseOf(String styling) {
		return styling.length() > 0 ? styling.substring(0,1).toUpperCase() + styling.substring(1) : "";
	}

}
