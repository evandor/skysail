package io.skysail.server.domain.jvm.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.JavaApplicationModel;
import io.skysail.server.domain.jvm.JavaEntityModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class JavaApplicationModelTest {

	@Data
	private class IdentifiableSupertype implements Identifiable {
		private String id;
	}

	@Data
	@EqualsAndHashCode(callSuper=false)
	private class IdentifiableSubtype extends IdentifiableSupertype {
	}

	@Test
	public void entity_supertype_relation_is_added_to_model() {
		SkysailServerResource<?> resourceClass = null;
		JavaEntityModel<IdentifiableSupertype> supertypeEntity = new JavaEntityModel<IdentifiableSupertype>(IdentifiableSupertype.class, resourceClass);

		JavaEntityModel<IdentifiableSubtype> subtypeEntity = new JavaEntityModel<IdentifiableSubtype>(IdentifiableSubtype.class, resourceClass);

		JavaApplicationModel app = new JavaApplicationModel("app17");
		app.addOnce(supertypeEntity);
		app.addOnce(subtypeEntity);

	    assertThat(app.getEntitySupertype(IdentifiableSubtype.class.getName()).getId(), is(IdentifiableSupertype.class.getName()));
	}
}
