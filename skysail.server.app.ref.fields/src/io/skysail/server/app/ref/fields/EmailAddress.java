package io.skysail.server.app.ref.fields;

import io.skysail.domain.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class EmailAddress implements ValueObject {

	private String id = String.valueOf(hashCode());

	private String value;

    @Override
    public void setValue(Object value) {
    	this.id = value.toString();
        this.value = value.toString();
    }
}
