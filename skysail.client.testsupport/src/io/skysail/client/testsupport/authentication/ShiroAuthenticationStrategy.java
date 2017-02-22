package io.skysail.client.testsupport.authentication;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;

import io.skysail.client.testsupport.ApplicationClient;

public class ShiroAuthenticationStrategy implements AuthenticationStrategy {

	@Override
	public ClientResource login(ApplicationClient<?> client, String username, String password) {
      ClientResource cr = new ClientResource(client.getBaseUrl() + "/_logout?targetUri=/");
      cr.get();
      cr = new ClientResource(client.getBaseUrl() + "/io.skysail.server.um.shiro.app.ShiroUmApplication/v1/_login");
      cr.setFollowingRedirects(true);
      Form form = new Form();
      form.add("username", username);
      form.add("password", password);
      cr.post(form, MediaType.TEXT_HTML);
      String credentials = cr.getResponse().getCookieSettings().getFirstValue("Credentials");
      cr = new ClientResource(client.getBaseUrl() + "/");
      cr.getCookies().add("Credentials", credentials);
      cr.get(MediaType.TEXT_HTML);
      return cr;
	}

}
