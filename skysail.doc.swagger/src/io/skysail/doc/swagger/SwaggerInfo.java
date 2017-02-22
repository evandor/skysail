package io.skysail.doc.swagger;

import com.fasterxml.jackson.annotation.JsonRootName;

import io.skysail.core.app.SkysailApplication;
import lombok.Getter;

@Getter
@JsonRootName("Info")
public class SwaggerInfo {

	private String title;
	private String description;
	private String version;
	private SwaggerContact contact;
	
	private SwaggerLicence license;

	public SwaggerInfo(SkysailApplication skysailApplication) {
		this.title = skysailApplication.getName();
		this.description = skysailApplication.getDescription();
		this.version = skysailApplication.getApiVersion().toString();
		this.contact = new SwaggerContact("skysail support", "http://www.skysail.io", "evandor@gmail.com");
		this.license = new SwaggerLicence("Apache License 2.0", "https://www.apache.org/licenses/LICENSE-2.0");
	}

}
