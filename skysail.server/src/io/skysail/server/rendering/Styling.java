package io.skysail.server.rendering;

import java.util.Optional;

import org.restlet.data.CookieSetting;

import io.skysail.server.Constants;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.CookiesUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@ToString(of = {"name","selected"})
@Getter
public class Styling implements Comparable<Styling> {

	private static final String DEFAULT_STYLING = "";

	private final String name; // e.g Bootstrap

	private final String link; // e.g ?_styling=bst

	private final String label;

	private String shortName;

	@Setter
	private boolean selected;

	public Styling(@NonNull String styling, @NonNull String shortName, boolean selected) {
		this.shortName = shortName;
		this.link = "?_styling="+shortName;
		this.name = styling;
		this.label = firstUppercaseOf(styling);
		this.selected = selected;
	}

	public static Styling determineForm(@NonNull SkysailServerResource<?> resource) {
		String stylingFromRequest = resource.getQuery() != null ? resource.getQuery().getFirstValue("_styling") : null;
		if (stylingFromRequest == null) {
			String styling = CookiesUtils.getStylingFromCookie(resource.getRequest()).orElse(DEFAULT_STYLING);
			return new Styling(styling, "", false);

		}
		Styling styling = new Styling(stylingFromRequest, stylingFromRequest, true);
		CookieSetting stylingCookie = CookiesUtils.createCookie(Constants.COOKIE_NAME_STYLING,
				resource.getRequest().getResourceRef().getPath(), -1);
		stylingCookie.setValue(stylingFromRequest);
		resource.getResponse().getCookieSettings().add(stylingCookie);
		return styling;
	}

	@Override
	public int compareTo(Styling o) {
		return name.compareTo(o.getName());
	}
	
	private String firstUppercaseOf(String styling) {
		return styling.length() > 0 ? styling.substring(0,1).toUpperCase() + styling.substring(1) : "";
	}

}
