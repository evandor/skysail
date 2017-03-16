package io.skysail.server.app.notes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

public class CategoriesResource extends ListServerResource<Category> {

	@Override
	public List<Category> getEntity() {
		Set<String> categoriesSet = getNotes().stream()
				.map(Note::getCategory)
				.collect(Collectors.toSet());
		return categoriesSet.stream()
					.map(c -> new Category(c, c))
					.sorted()
					.collect(Collectors.toList());
	}

	private List<Note> getNotes() {
		return ((NotesApplication) getApplication()).getRepo().find(new Filter(getRequest()));
	}

}
