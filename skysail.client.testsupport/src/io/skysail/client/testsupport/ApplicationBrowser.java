package io.skysail.client.testsupport;

import java.security.SecureRandom;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;

import io.skysail.client.testsupport.authentication.AuthenticationStrategy;
import io.skysail.client.testsupport.authentication.HttpBasicAuthenticationStrategy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ApplicationBrowser<T extends ApplicationBrowser<?, U>, U> {

    @Getter
    protected ApplicationBrowser parentEntityBrowser;

    protected SecureRandom random = new SecureRandom();

    @Getter
    @Setter
    private String id;

    protected static final String HOST = "http://localhost";

    protected MediaType mediaType;
    protected ApplicationClient<U> client;

    private String defaultUser = "default";
    private Integer port = 2014;

    @Getter
    private String url;

    @Getter
	private AuthenticationStrategy authenticationStrategy = new HttpBasicAuthenticationStrategy();

    public ApplicationBrowser(String url) {
        this(url, MediaType.TEXT_HTML, 2014);
    }

    public ApplicationBrowser(String appName, MediaType mediaType, int port) {
        this.mediaType = mediaType;
        url = HOST + ":" + port;
        log.info("{}creating new browser client with url '{}' for Application '{}' and mediaType '{}'",
                ApplicationClient.TESTTAG, url, appName, MediaType.TEXT_HTML);
        client = new ApplicationClient<>(url, appName, mediaType);
    }

    abstract protected Form createForm(U entity);

    public void setPort(String port) {
        this.port = Integer.parseInt(port);
    }

    protected String getBaseUrl() {
        return HOST + (port != null ? ":" + port : "");
    }

    public ApplicationBrowser<T, U> login() {
        return loginAs(defaultUser, "skysail");
    }

    protected ApplicationBrowser<T,U> loginAs(String username, String password) {
        log.info("{}logging in as user '{}'", ApplicationClient.TESTTAG, username);
        client.loginAs(getAuthenticationStrategy(), username, password);
        return this;
    }


    @SuppressWarnings("unchecked")
    public T asUser(String username) {
        this.defaultUser = username;
        login();
        return (T) this;
    }

    public void setUser(String defaultUser) {
        this.defaultUser = defaultUser;
        if (parentEntityBrowser != null) {
            this.parentEntityBrowser.setUser(defaultUser);
        }
    }

    public Status getStatus() {
        return client.getResponse().getStatus();
    }

    protected void findAndDelete(String id) {
        client.gotoAppRoot().followLinkTitleAndRefId("update", id).followLink(Method.DELETE);
    }

	public void setAuthenticationStrategy(AuthenticationStrategy strategy) {
		this.authenticationStrategy = strategy;
	}

}
