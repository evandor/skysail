package io.skysail.server.model;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.server.rendering.Styling;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class STStylingWrapper {

	ObjectMapper mapper = new ObjectMapper();
	
	private List<Styling> stylings;

	public STStylingWrapper(List<Styling> stylings) {
		this.stylings = stylings;
	}

	public String getAsJson() { //
		try {
			return mapper.writeValueAsString(stylings);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(),e);
			return "[]";
		}
	}
}
