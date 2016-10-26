package io.skysail.server.domain.jvm;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
public abstract class FieldFacet {

	@Getter
    private String id;

	@Getter
	private String name;

	public FieldFacet(@NonNull String id, @NonNull Map<String,String> config) {
    	this.id = id;
    	this.name = id.substring(1+id.lastIndexOf("."));
    }

    public abstract Map<String, AtomicInteger> bucketsFrom(Field field, List<?> list);

    public abstract String sqlFilterExpression(String value);

}
