package io.skysail.server.app.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.skysail.api.links.LinkRelation;
import io.skysail.core.model.SkysailEntityModel;
import io.skysail.domain.Entity;
import io.skysail.server.app.EntityFactory;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.Data;

public class EntityFactoryTest {

	private SkysailServerResource<?> resourceInstance;

	@Data
	private class AnIdentifiable implements Entity {
		private String id;
	}

	@Before
	public void setup() {
		resourceInstance = new ListServerResource<AnIdentifiable>() {
			@Override
			public List<AnIdentifiable> getEntity() {
				return Collections.emptyList();
			}
			@Override
			public LinkRelation getLinkRelation() {
				return null;
			}
		};
	}

	@Test
	public void resourceInstance_can_be_null() {
		SkysailEntityModel<AnIdentifiable> model = EntityFactory.createFrom(null, AnIdentifiable.class, null);
		checkDefaultAssertions(model);
	}

	@Test(expected = NullPointerException.class)
	public void identifiableClass_can_not_be_null() {
		EntityFactory.createFrom(null, null, resourceInstance);
	}

//	@Test
//	public void testName() {
//		SkysailEntityModel<AnIdentifiable> model = EntityFactory.createFrom(AnIdentifiable.class, resourceInstance);
//		checkDefaultAssertions(model);
//		assertThat(model.getAssociatedListResource().getResourceClass().toGenericString(), is(resourceInstance.getClass().toGenericString()));
//
//	}

	private void checkDefaultAssertions(SkysailEntityModel<AnIdentifiable> model) {
		assertThat(model.getId(),is(AnIdentifiable.class.getName()));
		assertThat(model.getIdentifiableClass().toGenericString(),is(AnIdentifiable.class.toGenericString()));
		assertThat(model.isAggregate(), is(true));
		assertThat(model.getFieldNames().size(), is(0));
	}
}

