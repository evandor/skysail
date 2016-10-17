package io.skysail.server.domain.jvm;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class FieldFacet {

    protected Field field;

    public FieldFacet(Map<String,String> config) {}

    public abstract Map<Integer, AtomicInteger> bucketsFrom(List<?> list);

}
