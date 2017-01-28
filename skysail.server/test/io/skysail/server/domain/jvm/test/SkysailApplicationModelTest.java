package io.skysail.server.domain.jvm.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.skysail.domain.Entity;
import io.skysail.server.domain.jvm.SkysailApplicationModel;
import io.skysail.server.domain.jvm.SkysailEntityModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class SkysailApplicationModelTest {

	@Data
	private class EntitySupertype implements Entity {
		private String id;
	}

	@Data
	@EqualsAndHashCode(callSuper=false)
	private class EntitySubtype extends EntitySupertype {
	}

//	@Test
//	public void entity_supertype_relation_is_added_to_model() {
//		SkysailServerResource<?> resourceClass = null;
//		SkysailEntityModel<IdentifiableSupertype> supertypeEntity = new SkysailEntityModel<IdentifiableSupertype>(IdentifiableSupertype.class, resourceClass);
//
//		SkysailEntityModel<IdentifiableSubtype> subtypeEntity = new SkysailEntityModel<IdentifiableSubtype>(IdentifiableSubtype.class, resourceClass);
//
//		SkysailApplicationModel app = new SkysailApplicationModel("app17");
//		app.addOnce(supertypeEntity);
//		app.addOnce(subtypeEntity);
//
//	    assertThat(app.getEntitySupertype(IdentifiableSubtype.class.getName()).getId(), is(IdentifiableSupertype.class.getName()));
//	}
}
