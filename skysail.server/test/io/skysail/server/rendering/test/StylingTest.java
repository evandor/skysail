package io.skysail.server.rendering.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CookieSetting;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.util.Series;

import io.skysail.core.resources.SkysailServerResource;
import io.skysail.server.rendering.Styling;

import static org.mockito.Mockito.*;

public class StylingTest {

	SkysailServerResource<?> resource = Mockito.mock(SkysailServerResource.class);
	Request request = Mockito.mock(Request.class);
	Response response = Mockito.mock(Response.class);
	Reference resourceRef = Mockito.mock(Reference.class);

	@Test(expected = NullPointerException.class)
	public void styling_from_null_request_throws_exceptiont() {
		 Styling.determineForm(null);
	}
	
	@Test
	public void styling_from_empty_request_returns_default() {
		assertThat(Styling.determineForm(resource).getName(), is(""));
	}
	
	@Test
	public void styling_is_derived_from_parameter_and_saved_to_cookie() {
		Form query = new Form();
		query.add("_styling","semanticui");
		
		@SuppressWarnings("unchecked")
		Series<CookieSetting> cookieSettings = Mockito.mock(Series.class);

		Mockito.when(resource.getQuery()).thenReturn(query);
		Mockito.when(resource.getRequest()).thenReturn(request);
		Mockito.when(resource.getResponse()).thenReturn(response);
		Mockito.when(response.getCookieSettings()).thenReturn(cookieSettings);
		Mockito.when(request.getResourceRef()).thenReturn(resourceRef);
		Mockito.when(resourceRef.getPath()).thenReturn("thePath");

		Styling style = Styling.determineForm(resource);
		
		assertThat(style.getName(), is("semanticui"));
		verify(cookieSettings,times(1)).add(any());
	}
}
