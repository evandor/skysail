package io.skysail.server.app.resources.swagger;

import com.fasterxml.jackson.annotation.JsonRootName;

import io.skysail.server.domain.jvm.SkysailApplicationModel;
import lombok.Getter;

@Getter
@JsonRootName("Info")
public class SwaggerInfo {

	private String title;
	private String description;
	private String version;
	private Contact contact;
	
	private SwaggerLicence license;

	public SwaggerInfo(SkysailApplicationModel applicationModel) {
		this.title = applicationModel.getName();
		this.description = "Skysail Application";
		this.version = applicationModel.getSkysailApplication().getApiVersion().toString();
		this.contact = new Contact("API Support", "http://www.skysail.io", "evandor@gmail.com");
		this.license = new SwaggerLicence("MIT", "link");
	}

}
