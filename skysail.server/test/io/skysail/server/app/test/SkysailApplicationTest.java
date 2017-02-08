package io.skysail.server.app.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.restlet.service.CorsService;

import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.SkysailApplication;

@RunWith(MockitoJUnitRunner.class)
public class SkysailApplicationTest {
	private SkysailApplication skysailApplication = new SkysailApplication("name") {
		
		public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext) throws ConfigurationException {
			super.activate(appConfig, componentContext);
		};
	};

	@Test
	public void no_cors_service_is_created_for_empty_cors_configuration() throws ConfigurationException {
		skysailApplication.activate(createAppConfig(null, null, null, null),null);
		assertThat(skysailApplication.getServices().get(CorsService.class), is(nullValue()));
	}

	@Test
	public void cors_service_is_created_if_set_in_cors_configuration() throws ConfigurationException {
		skysailApplication.activate(createAppConfig(new String[] {"*"},null,null, null),null);
		assertThat(skysailApplication.getServices().get(CorsService.class), is(notNullValue()));
	}

	@Test
	public void cors_service_is_created_correctly_for_default_configuration() throws ConfigurationException {
		skysailApplication.activate(createAppConfig(new String[] {"*"},null,null, null),null);
		CorsService corsService = skysailApplication.getServices().get(CorsService.class);
		assertThat(corsService.getAllowedOrigins().toString(),is("[*]"));
		assertThat(corsService.getAllowedHeaders(),is(nullValue()));
		assertThat(corsService.getExposedHeaders(),is(nullValue()));
		assertThat(corsService.isAllowedCredentials(),is(false));		
	}

	@Test
	public void allowed_headers_are_set_if_configured() throws ConfigurationException {
		skysailApplication.activate(createAppConfig(new String[] {"*"},new String[] {"x-amz-*","x-abc-*"}, null,null),null);
		CorsService corsService = skysailApplication.getServices().get(CorsService.class);
		assertThat(corsService.getAllowedHeaders().toString(),containsString("x-amz-*"));
		assertThat(corsService.getAllowedHeaders().toString(),containsString("x-abc-*"));
	}

	@Test
	public void exposed_headers_are_set_if_configured() throws ConfigurationException {
		skysailApplication.activate(createAppConfig(new String[] {"*"},null, new String[] {"xxx","yyy"}, null),null);
		CorsService corsService = skysailApplication.getServices().get(CorsService.class);
		assertThat(corsService.getExposedHeaders().toString(),containsString("xxx"));
		assertThat(corsService.getExposedHeaders().toString(),containsString("yyy"));
	}

	@Test
	public void allowCredentials_is_set_if_configured() throws ConfigurationException {
		skysailApplication.activate(createAppConfig(new String[] {"*"},null, new String[] {"xxx","yyy"}, "true"),null);
		CorsService corsService = skysailApplication.getServices().get(CorsService.class);
		assertThat(corsService.isAllowedCredentials(),is(true));		
	}

	private ApplicationConfiguration createAppConfig(
			String[] origins, 
			String[] allowedHeaders, 
			String[] exposedHeaders,
			String allowCredentials) {
	
		return new ApplicationConfiguration() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}
			
			@Override
			public String[] corsOrigins() {
				return origins;
			}
			
			@Override
			public String[] corsMethods() {
				return null;
			}
			
			@Override
			public String[] corsExposedHeaders() {
				return exposedHeaders;
			}
			
			@Override
			public String[] corsAllowedHeaders() {
				return allowedHeaders;
			}
			
			@Override
			public String corsAllowCredentials() {
				return allowCredentials;
			}

			@Override
			public String host() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

}
