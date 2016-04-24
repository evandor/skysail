package io.skysail.domain.core.resources;

import io.skysail.domain.Identifiable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class Resource<T extends Identifiable>  {

}
