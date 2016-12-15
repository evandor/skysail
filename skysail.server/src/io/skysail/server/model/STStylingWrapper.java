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
		//[{"name":"bootstrap","shortName":"bst","label":"Bootstrap","selected":false},{"name":"jquerymobile","shortName":"jqm","label":"Jquerymobile","selected":false},{"name":"mdb","shortName":"mdb","label":"Mdb","selected":false},{"name":"semanticui","shortName":"sui","label":"Semanticui","selected":false}]
				
		//return "[{\"label\":\"default_1\",\"selected\":true,\"link\":\"thelink\"},{\"label\":\"default_1\",\"selected\":true,\"link\":\"thelink\"}]";
	}
}
