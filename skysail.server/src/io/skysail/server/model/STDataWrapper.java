package io.skysail.server.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class STDataWrapper {

	private ObjectMapper mapper = new ObjectMapper();

	private List<Map<String, Object>> data;

	public STDataWrapper(List<Map<String, Object>> data) {
		 this.data = data.stream().map(HashMap<String,Object>::new).collect(Collectors.toList());
		 this.data.forEach(m -> m.remove("_links"));
	}

	public String getAsJson() { //
		try {
			return mapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
			return "[]";
		}
	}

}
