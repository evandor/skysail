package io.skysail.server.forms;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.skysail.api.responses.ConstraintViolationDetails;
import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.domain.core.FieldModel;
import io.skysail.domain.html.IgnoreSelectionProvider;
import io.skysail.domain.html.InputType;
import io.skysail.domain.html.Reference;
import io.skysail.domain.html.SelectionProvider;
import io.skysail.domain.html.Submit;
import io.skysail.server.domain.jvm.SkysailApplicationService;
import io.skysail.server.model.DefaultEntityFieldFactory;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.um.domain.SkysailUser;
import io.skysail.server.utils.ReflectionUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * A FormField instance encapsulates (meta) information which can be used to
 * display a single field in a (web) form, or a column in a table
 * representation.
 *
 * <p>
 * A FormField is constructed from a java.lang.reflect.Field, together with a
 * SkysailServerResource; only {@link FieldModel}s and {@link Reference}s should
 * be taken into account.
 * </p>
 *
 * <p>
 * This class will not try to determine the actual value of the field, this is
 * left to further processing.
 * </p>
 *
 */
@Slf4j
@ToString(callSuper = true, of = {"entityType", "nestedTable"})
public class FormField extends io.skysail.domain.core.FieldModel {

    /**
     * String for an annotated Field of type String; Identifiable for an annotated Field of type List<Identifiable>.
     */
    @Getter
    private Type entityType;
    
    @Getter
    private ListView listViewAnnotation;

    @Getter
    private PostView postViewAnnotation;

    @Getter
    private Object currentEntity;

    @Getter
    private String violationMessage;

    /**
     * if set to true, the field will be rendered in such a way that the form
     * will be submitted when clicking
     */
    @Setter
    private boolean submitField;

    private Reference referenceAnnotation;
    
    @Getter
    private FieldRelationInfo fieldRelation;
    
    private io.skysail.domain.html.Field formFieldAnnotation;
    private Submit submitAnnotation;
    private NotNull notNullAnnotation;
    private Size sizeAnnotation;
    private List<Option> selectionOptions;

    @Getter
    private List<FormField> nestedTable;
    
    @Getter
    private String tab;

	private SkysailApplicationService appService;
	
	@Getter
	private String htmlId;

    @Getter
    private String htmlName;

    public FormField(Field field, Object currentEntity, SkysailApplicationService appService) {
        super(field.getName(), field.getType());
		this.appService = appService;
        setEntityClass(field);
        setInputType(getFromFieldAnnotation(field));
        setAnnotations(field);
        this.currentEntity = currentEntity;
        if (InputType.TABLE.equals(inputType)) {
            Type listFieldGenericType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            nestedTable = new ArrayList<>( new DefaultEntityFieldFactory((Class<?>)listFieldGenericType).determine(currentEntity).values());
        }
        tab = postViewAnnotation != null ? postViewAnnotation.tab() : null;
        this.htmlId = field.getDeclaringClass().getName().replace(".","_") + "_" + field.getName();
        this.htmlName = field.getDeclaringClass().getName() + "|" + field.getName();
    }

    public FormField(Field field, SkysailServerResource<?> resource, ConstraintViolationsResponse<?> source) {
        this(field, resource, (SkysailApplicationService)null);
        Set<ConstraintViolationDetails> violations = ((ConstraintViolationsResponse<?>) source).getViolations();
        Optional<String> validationMessage = violations.stream()
                .filter(v -> v.getPropertyPath().equals(field.getName())).map(ConstraintViolationDetails::getMessage).findFirst();
        violationMessage = validationMessage.orElse(null);
    }
    
//    public String getHtmlId() {
//    	return getEntityClassName().replace(".", "_") + "_" + getId();
//    }
    
    private void setEntityClass(Field field) {
        this.entityType = field.getType();
        if (Collection.class.isAssignableFrom(field.getType())) {
            this.entityType = ReflectionUtils.getParameterizedType(field);
        }
    }

    private void setAnnotations(Field field) {
        referenceAnnotation = field.getAnnotation(Reference.class);
        fieldRelation = analyseFieldRelation();
        formFieldAnnotation = field.getAnnotation(io.skysail.domain.html.Field.class);
        listViewAnnotation = field.getAnnotation(ListView.class);
        postViewAnnotation = field.getAnnotation(PostView.class);
        submitAnnotation = field.getAnnotation(Submit.class);
        notNullAnnotation = field.getAnnotation(NotNull.class);
        sizeAnnotation = field.getAnnotation(Size.class);
    }

    private FieldRelationInfo analyseFieldRelation() {
        return new FieldRelationInfo(this, appService);
    }

    public String getMessageKey() {
        return MessagesUtils.getBaseKey(currentEntity.getClass(), this) + ".desc";
    }

    public String getNameKey() {
        if (currentEntity == null) {
            return getId();
        }
        if (currentEntity instanceof List && ((List<?>) currentEntity).size() > 0) {
            return MessagesUtils.getBaseKey(((List<?>) currentEntity).get(0).getClass(), this);
        }
        return MessagesUtils.getBaseKey(currentEntity.getClass(), this);
    }

    public String getPlaceholderKey() {
        return MessagesUtils.getBaseKey(currentEntity.getClass(), this) + ".placeholder";
    }

    public String getTitleKey() {
        return MessagesUtils.getBaseKey(currentEntity.getClass(), this) + ".title";
    }

    public String getHref() {
        return null;
    }
    
    public List<String> getFieldAttributes() {
        return Arrays.asList(formFieldAnnotation.fieldAttributes());
    }

    public boolean isDateType() {
        return checkTypeFor(Date.class);
    }

    public boolean isRangeType() {
        return isOfInputType(InputType.RANGE);
    }

    public boolean isSkysailUserType() {
        return checkTypeFor(SkysailUser.class);
    }

    public boolean isTagsInputType() {
        return isOfInputType(InputType.TAGS);
    }

    public boolean isMultiselectInputType() {
        return isOfInputType(InputType.MULTISELECT);
    }

    public boolean isReadonlyInputType() {
        return isOfInputType(InputType.READONLY);
    }

    public boolean isMarkdownEditorInputType() {
        return isOfInputType(InputType.MARKDOWN_EDITOR);
    }

    public boolean isTrixEditorInputType() {
        return isOfInputType(InputType.TRIX_EDITOR);
    }

    public boolean isTextareaInputType() {
        return isOfInputType(InputType.TEXTAREA);
    }

    public boolean isPolymerInputType() {
        return isOfInputType(InputType.POLYMER);
    }

    public boolean isSelectionProvider() {

        if (formFieldAnnotation != null) {
            Class<? extends SelectionProvider> selectionProvider = formFieldAnnotation.selectionProvider();
            if (!(selectionProvider.equals(IgnoreSelectionProvider.class))) {
                return true;
            }
        }
        if (referenceAnnotation == null) {
            return false;
        }
        if (!(IgnoreSelectionProvider.class.equals(referenceAnnotation.selectionProvider()))) {
            return true;
        }
        return false;
    }

    public boolean isSubmitField() {
        return submitAnnotation != null;
    }

    public boolean isCheckbox() {
        if (formFieldAnnotation != null) {
            return InputType.CHECKBOX.equals(formFieldAnnotation.inputType());
        }
        return false;
    }

    public boolean isDate() {
        if (formFieldAnnotation != null) {
            return InputType.DATE.equals(formFieldAnnotation.inputType());
        }
        return false;
    }

    public boolean isRange() {
        if (formFieldAnnotation != null) {
            return InputType.RANGE.equals(formFieldAnnotation.inputType());
        }
        return false;
    }
    
    public String getEventsCode() {
    	StringBuilder sb = new StringBuilder();
    	if (formFieldAnnotation != null && formFieldAnnotation.onEvent().length > 0) {
    		Arrays.stream(formFieldAnnotation.onEvent())
    			.filter(eventDefinition -> eventDefinition.trim().length() > 0)
    			.forEach(eventDefinition -> {
    				String[] split = eventDefinition.split(":",2);
    				String event = split[0];//"on" + split[0].substring(0, 1).toUpperCase() + split[0].substring(1);
    				sb.append(event).append("=\"").append(split[1]).append("\"");
    			});
    	}
		return sb.toString();
    }
    
    public String getDynamicAttributes() {
        StringBuilder sb = new StringBuilder();
        if (formFieldAnnotation != null && formFieldAnnotation.onEvent().length > 0) {
            Arrays.stream(formFieldAnnotation.onEvent())
                .filter(eventDefinition -> eventDefinition.trim().length() > 0)
                .forEach(eventDefinition -> {
                    String[] split = eventDefinition.split(":",2);
                    String event = split[0];//"on" + split[0].substring(0, 1).toUpperCase() + split[0].substring(1);
                    sb.append(event).append("='").append(split[1]).append("'");
                });
        }
        return sb.toString();
    }

    public int getRangeMin() {
        return 0;
    }

    public int getRangeMax() {
        return 100;
    }

    public boolean isLink() {
        if (listViewAnnotation != null) {
            return !listViewAnnotation.link().toString().equals("class io.skysail.server.forms.ListView$DEFAULT");
        }
        return false;
    }
    
    public String getEntityClassName() {
        return this.currentEntity.getClass().getName();
    }

//    public List<Option> getSelectionProviderOptions() {
//        if (!isSelectionProvider()) {
//            throw new IllegalAccessError("not a selection provider");
//        }
//        if (selectionOptions != null) {
//            return selectionOptions;
//        }
//        List<Option> options = new ArrayList<>();
//
//        Class<? extends SelectionProvider> selectionProvider = null;
//        if (formFieldAnnotation != null) {
//            selectionProvider = formFieldAnnotation.selectionProvider();
//        }
//
//        if (referenceAnnotation != null) {
//            selectionProvider = referenceAnnotation.selectionProvider();
//        }
//        if (selectionProvider == null) {
//            return Collections.emptyList();
//        }
//        SelectionProvider selection;
//        try {
//            Method method = selectionProvider.getMethod("getInstance");
//            selection = (SelectionProvider) method.invoke(selectionProvider, new Object[] {});
//
//            String value = getSelectedValue();
//            method = selectionProvider.getMethod("setResource", Resource.class);
//            method.invoke(selection, resource);
//            selection.getSelections().entrySet().stream().forEach(entry -> {
//                options.add(new Option(entry, value));
//            });
//            if (!options.stream().filter(Option::isSelected).findFirst().isPresent()) {
//                options.get(0).setSelected(true);
//            }
//
//            selectionOptions = options;
//            return options;
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Collections.emptyList();
//    }

    private String getSelectedValue() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String value = "";
        if (currentEntity != null) {
            Method method2 = currentEntity.getClass()
                    .getMethod("get" + getId().substring(0, 1).toUpperCase() + getId().substring(1));
            Object methodCall = method2.invoke(currentEntity);
            if (methodCall != null) {
                value = methodCall.toString();
            }
        }
        return value;
    }

    @Override
    public boolean isMandatory() {
        if (notNullAnnotation != null) {
            return true;
        }
        if (sizeAnnotation != null) {
            int min = sizeAnnotation.min();
            if (min > 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isOfInputType(InputType inputType) {
        return this.inputType.equals(inputType);
    }

    private boolean checkTypeFor(Class<?> cls) {
        return getType() != null && getType().equals(cls);
    }

    private InputType getFromFieldAnnotation(Field fieldAnnotation) {
        io.skysail.domain.html.Field annotation = fieldAnnotation.getAnnotation(io.skysail.domain.html.Field.class);
        return annotation != null ? annotation.inputType() : null;
    }

}
