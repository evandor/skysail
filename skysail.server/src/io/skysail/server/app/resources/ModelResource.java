package io.skysail.server.app.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.domain.jvm.SkysailApplicationModel;
import io.skysail.server.restlet.resources.EntityServerResource;

/**
 * Default resource, attached to path "/".
 *
 */
// TODO use GenericIdentifiable once annotated with @Field
public class ModelResource extends EntityServerResource<IdentifiableString> {


	@Override
	public SkysailResponse<?> eraseEntity() {
		return null;
	}

	@Override
	public IdentifiableString getEntity() {
		SkysailApplicationModel applicationModel = getApplication().getApplicationModel();
		return new IdentifiableString("<pre>" + applicationModel.toString() + "</pre>");
	}

}
