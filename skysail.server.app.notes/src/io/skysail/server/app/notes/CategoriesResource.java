package io.skysail.server.app.notes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

public class CategoriesResource extends ListServerResource<Category> {

	@Override
	public List<?> getEntity() {
		List<Note> notes = ((NotesApplication) getApplication()).getRepo().find(new Filter(getRequest()));
		Set<String> categoriesSet = notes.stream().map(n -> n.getCategory()).collect(Collectors.toSet());
		return categoriesSet.stream()
					.map(c -> new Category(c, c))
					.sorted()
					.collect(Collectors.toList());
	}

}
