package io.skysail.server.app.esclient;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.restlet.resource.ClientResource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.domain.Identifiable;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.esclient.resources.IndicesResource;
import io.skysail.server.app.esclient.resources.IndicesResource2;
import io.skysail.server.app.esclient.resources.MappingsResource;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Slf4j
public class ElastisearchClientApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

	public static final String APP_NAME = "elasticsearch";

	private ObjectMapper mapper = new ObjectMapper();

	@Reference
	private DbService dbService;

	// @Getter
	// private TemplateRepository repo;

	public ElastisearchClientApplication() {
		super(APP_NAME, new ApiVersion(1));
		setDescription("a skysail application");
		//mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
	}

	@Activate
	@Override
	public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
			throws ConfigurationException {
		super.activate(appConfig, componentContext);
		// this.repo = new TemplateRepository(dbService);
	}

	@Override
	protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
		securityConfigBuilder.authorizeRequests().startsWithMatcher("").authenticated();
	}

	@Override
	protected void attach() {
		super.attach();

		router.attach(new RouteBuilder("", IndicesResource.class));
		router.attach(new RouteBuilder("/indices", IndicesResource2.class));
		router.attach(new RouteBuilder("/indices/{id}/mappings", MappingsResource.class));
	}

	public List<?> get(String apiUrl, Class<? extends Identifiable> cls) {
		try {
			String text = new ClientResource(apiUrl).get().getText();
			return deserializeJson(text, cls);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}
	
	public List<?> getCollection(String apiUrl, Class<? extends Identifiable> cls) {
		try {
			String text = new ClientResource(apiUrl).get().getText();
			return deserializeJsonCollection(text, cls);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}
	
	private List<?> deserializeJson(String json, Class<? extends Identifiable> cls) throws IOException {
		JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, cls);
		return mapper.readValue(json, type);
	}
	
	private List<?> deserializeJsonCollection(String json, Class<? extends Identifiable> cls) throws IOException {
		JavaType type = mapper.getTypeFactory().constructCollectionType(Collection.class, cls);
		return mapper.readValue(json, type);
	}


}