package io.skysail.server.model;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.api.text.Translation;
import io.skysail.domain.html.InputType;
import io.skysail.server.forms.FieldRelationInfo;
import io.skysail.server.forms.FormField;
import io.skysail.server.services.PolymerElementDefinition;
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
        private String dynamicAttributes;
        private Class<?> type;
        private List<String> fieldAttributes;
        private Object currentEntity;
        private String helpMsg;
		private String placeholder;

		public FormFieldAdapter(FormField ff) {
			label = determineText(ff, ff.getId());
		    id = ff.getHtmlId();
			name = ff.getHtmlName();
			inputType = ff.getInputType().name();
			type=ff.getType();
			//tag = ff.getTag();
			nestedTable = ff.getNestedTable();
			tab = ff.getTab();
			fieldRelation = ff.getFieldRelation();
			eventsCode = ff.getEventsCode();
			dynamicAttributes = ff.getDynamicAttributes();

			fieldAttributes = ff.getFieldAttributes();
			currentEntity = ff.getCurrentEntity();

			helpMsg = determineText(ff, ff.getId() + ".desc");
			placeholder = determineText(ff, ff.getId() + ".placeholder");
		}

        private String determineText(FormField ff, String key) {
            Translation translation = STFormFieldsWrapper.this.messages.get(key);
            if (translation == null || translation.getValue() == null) {
                return "";//ff.getLabel();
            }
            return translation.getValue();
        }

	    public boolean isPolymerInputType() {
	        return isOfInputType(InputType.POLYMER);
	    }

	    private boolean isOfInputType(InputType inputType) {
	        return this.inputType.equals(inputType.name());
	    }

	    public String getRenderedPolymer() {
	        try {
                PolymerElementDefinition newInstance = (PolymerElementDefinition)type.newInstance();

                newInstance.setFormFieldAdapter(this);
                newInstance.setMessages(STFormFieldsWrapper.this.messages);

                fieldAttributes.forEach(attributeName -> {
                    try {
                        Field declaredField = newInstance.getClass().getDeclaredField(attributeName);
                        if (declaredField != null) {
                            declaredField.setAccessible(true);
                            STFormFieldsWrapper.this.formfields.stream()
                                .filter(ff -> ff.getName().endsWith(attributeName))
                                .findFirst().ifPresent(ff -> {
                                    try {
                                        Field valueField = currentEntity.getClass().getDeclaredField(ff.getLabel());
                                        valueField.setAccessible(true);
                                        Object value = valueField.get(currentEntity);
                                        declaredField.set(newInstance, value.toString());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
                return newInstance.render();
            } catch (Exception e) { // NOSONAR
            	log.error(e.getMessage(), e);
                return e.getMessage();
            }
	    }

	}

	ObjectMapper mapper = new ObjectMapper();

	@Getter
	private List<FormFieldAdapter> formfields;

	private Map<String, Translation> messages;

	public STFormFieldsWrapper(List<FormField> collection, Map<String, Translation> messages) {
		this.messages = messages;
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
