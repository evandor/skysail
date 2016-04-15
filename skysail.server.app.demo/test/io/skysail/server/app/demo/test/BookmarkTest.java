package io.skysail.server.app.demo.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

import io.skysail.server.app.demo.Bookmark;

public class BookmarkTest {

	@Test
	public void testName() {
		Bookmark bookmark = new Bookmark();
		bookmark.setId("id");
		assertThat(bookmark.getId(),is("id"));
	}
}
