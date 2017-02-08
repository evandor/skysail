package io.skysail.server.app.resources;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.skysail.api.text.Translation;
import io.skysail.core.app.SkysailApplication;
import io.skysail.core.model.SkysailApplicationModel;
import io.skysail.server.restlet.resources.ListServerResource;

/**
 * Default resource, attached to path "/".
 *
 */
public class I18NResource extends ListServerResource<Message> {

	private String id;
	private SkysailApplication app;

	@Override
	protected void doInit() {
		id = getAttribute("id");
		app = (SkysailApplication) getApplication();
	}

	@Override
	public List<?> getEntity() {
		SkysailApplicationModel applicationModel = getApplication().getApplicationModel();

		Map<String, Translation> msgs = getMessages();

		applicationModel.getEntityValues().stream().forEach(entity -> {
			entity.getFieldNames().forEach(field -> {

				String baseKey = entity.getId()+ "." + field;
				addTranslation(msgs, getApplication(), baseKey, field);
				addTranslation(msgs, getApplication(), baseKey + ".desc", null);
				addTranslation(msgs, getApplication(), baseKey + ".placeholder", null);

				/*
				 * String resourceBaseKey = this.getClass().getName() + "." +
				 * fieldName; //
				 * io.skysail.server.app.notes.resources.PostNoteResource.
				 * content addTranslation(msgs, getApplication(),
				 * resourceBaseKey, fieldName); addTranslation(msgs,
				 * getApplication(), resourceBaseKey + ".desc", null);
				 * addTranslation(msgs, getApplication(), resourceBaseKey +
				 * ".placeholder", null);
				 */

			});
		});
		
		return msgs.keySet().stream()
				.map(key -> new Message(key, msgs.get(key)))
				.collect(Collectors.toList());
	}

}
