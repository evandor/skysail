package io.skysail.server.model;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.server.forms.FormField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class STFormFieldsWrapper {

	@Data
	public class FormFieldAdapter {

		private String name;
		private String inputType;
        private List<FormField> nestedTable;

		public FormFieldAdapter(FormField ff) {
			name = ff.getName();
			inputType = ff.getInputType();
			nestedTable = ff.getNestedTable();
		}
	}

	ObjectMapper mapper = new ObjectMapper();
	
	private List<FormFieldAdapter> formfields;

	public STFormFieldsWrapper(List<FormField> collection) {
		this.formfields = collection.stream().map(FormFieldAdapter::new).collect(Collectors.toList());
	}

	public String getAsJson() { //
		try {
			return mapper.writeValueAsString(formfields);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(),e);
			return "[]";
		}
	}
}
