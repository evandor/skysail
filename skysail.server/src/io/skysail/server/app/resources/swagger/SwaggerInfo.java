package io.skysail.server.app.resources.swagger;

import com.fasterxml.jackson.annotation.JsonRootName;

import io.skysail.server.app.SkysailApplication;
import lombok.Getter;

@Getter
@JsonRootName("Info")
public class SwaggerInfo {

	private String title;
	private String description;
	private String version;
	private Contact contact;
	
	private SwaggerLicence license;

	public SwaggerInfo(SkysailApplication skysailApplication) {
		this.title = skysailApplication.getName();
		this.description = skysailApplication.getDescription();
		this.version = skysailApplication.getApiVersion().toString();
		this.contact = new Contact("API Support", "http://www.skysail.io", "evandor@gmail.com");
		this.license = new SwaggerLicence("Apache License 2.0", "https://www.apache.org/licenses/LICENSE-2.0");
	}

}
