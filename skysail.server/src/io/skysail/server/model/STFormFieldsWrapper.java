package io.skysail.server.model;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.server.Constants;
import io.skysail.server.forms.FieldRelationInfo;
import io.skysail.server.forms.FormField;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class STFormFieldsWrapper {

	@Data
	@ToString
	public class FormFieldAdapter {

		private String id;
		private String name;
		private String label;
		private String inputType;
        private List<FormField> nestedTable;
		private String tab;
		private String tag;
        private FieldRelationInfo fieldRelation;
		private String eventsCode;

		public FormFieldAdapter(FormField ff) {
			label = ff.getName();
		    id = ff.getEntityClassName().replace(".", "_") + "_" + ff.getName();
			name = ff.getEntityClassName() + Constants.CLASS_FIELD_NAMES_SEPARATOR + ff.getName();;
			inputType = ff.getInputType();
			tag = ff.getTag();
			nestedTable = ff.getNestedTable();
			tab = ff.getTab();
			fieldRelation = ff.getFieldRelation();
			eventsCode = ff.getEventsCode();
		}
	}

	ObjectMapper mapper = new ObjectMapper();
	
	@Getter
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
