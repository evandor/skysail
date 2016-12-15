package io.skysail.server.app.esclient.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Data;
import lombok.Getter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class EsMapping implements Identifiable {

	@Field
	private String id;

	public EsMapping(String id) {
		this.id = id;
	}
	
	public void setId(String id) {
	}

}
