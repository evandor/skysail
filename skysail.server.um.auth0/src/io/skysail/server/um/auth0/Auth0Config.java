package io.skysail.server.um.auth0;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Auth0 Config", pid="auth0Config")
public @interface Auth0Config {


	// @ConditionalOnProperty(prefix = "auth0", name = "servletFilterEnabled")
	// @Bean
	// public FilterRegistrationBean filterRegistration() {
	// final FilterRegistrationBean registration = new FilterRegistrationBean();
	// registration.setFilter(new Auth0Filter(this));
	// registration.addUrlPatterns(securedRoute);
	// registration.addInitParameter("redirectOnAuthError",
	// loginRedirectOnFail);
	// registration.setName("Auth0Filter");
	// return registration;
	// }

	String domain();
	String clientId();
	String clientSecret();
	String onLogoutRedirectTo();
	String loginRedirectOnSuccess();
	String loginRedirectOnFail();
	String loginCallback();
	String securedRoute();

}
