package io.skysail.server.app.esclient.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsIndex implements Entity {

	public String getId() {
		return index;
	}

	public void setId(String id) {
	}

	@Field(inputType = InputType.READONLY)
	private String index, health, status, pri, rep;
	
}
