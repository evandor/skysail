package io.skysail.server.domain.jvm.facets.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import io.skysail.server.domain.jvm.facets.YearFacet;

@RunWith(MockitoJUnitRunner.class)
public class YearFacetTest {

	public class Dummy {
		// @Facet(type = FacetType.YEAR)
		private Date date = new Date();

		public Dummy(Date date) {
			this.date = date;
		}
	}

	private YearFacet yearFacet;
	private Field field;

	@Before
	public void setUp() throws Exception {
		field = Dummy.class.getDeclaredField("date");
		yearFacet = new YearFacet("id", Collections.emptyMap());
	}

	@Test
	@Ignore
	public void testBucketsFrom() throws Exception {
		Map<Integer, AtomicInteger> buckets = yearFacet.bucketsFrom(field,Arrays.asList(new Dummy(new Date()),
				new Dummy(new Date()),
				new Dummy(Date.from(LocalDate.now().minusYears(2).atStartOfDay(ZoneId.systemDefault()).toInstant()))));
		assertThat(buckets.size(), is(2));
		assertThat(buckets.get(2016).get(), is(2));
		assertThat(buckets.get(2014).get(), is(1));
	}

}
