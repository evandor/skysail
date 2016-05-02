package io.skysail.domain.test;

import io.skysail.domain.core.FieldModel;

public class StepDefs {

	protected Object propertyValue(FieldModel obj, String propertyName) throws Exception {
		return obj.getClass().getMethod("get"+propertyName.substring(0,1).toUpperCase() + propertyName.substring(1)).invoke(obj);
	}

}
