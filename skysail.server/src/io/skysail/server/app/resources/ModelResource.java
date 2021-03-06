package io.skysail.server.app.resources;

import io.skysail.core.model.SkysailApplicationModel;
import io.skysail.server.restlet.resources.EntityServerResource;

/**
 * Default resource, attached to path "/".
 *
 */
// TODO use GenericIdentifiable once annotated with @Field
public class ModelResource extends EntityServerResource<IdentifiableString> {

	@Override
	public IdentifiableString getEntity() {
		SkysailApplicationModel applicationModel = getApplication().getApplicationModel();
		return new IdentifiableString("<pre>" + applicationModel.toString() + "</pre>");
	}

}
