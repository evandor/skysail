package io.skysail.server.app.resources;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.restlet.Application;

import io.skysail.api.text.Translation;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.domain.jvm.SkysailApplicationModel;
import io.skysail.server.forms.MessagesUtils;
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
				System.out.println(entity.getId() + "." + field);

				String baseKey = entity.getId()+ "." + field; // MessagesUtils.getBaseKey(entityClass,
																	// f); //
																	// io.skysail.server.app.notes.Note.title
				String fieldName = field;
				addTranslation(msgs, getApplication(), baseKey, fieldName);
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
		
		return msgs.keySet().stream().map(key -> new Message(key, msgs.get(key))).collect(Collectors.toList());
	}

}
