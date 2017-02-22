package io.skysail.server.app.ref.fields;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.skysail.domain.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class EmailAddress implements ValueObject {

    private String id = String.valueOf(hashCode());

    private String value;

    @JsonCreator(mode = Mode.DELEGATING) // https://github.com/FasterXML/jackson-databind/issues/1318
    public EmailAddress(@JsonProperty("value") String value) {
        this.value = value;
    }

    @Override
    public void setValue(Object value) {
        this.id = value.toString();
        this.value = value.toString();
    }
}
