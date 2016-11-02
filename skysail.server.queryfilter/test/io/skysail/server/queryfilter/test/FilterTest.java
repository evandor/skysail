package io.skysail.server.queryfilter.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.domain.jvm.facets.YearFacet;
import io.skysail.server.queryfilter.Filter;
import lombok.Data;

public class FilterTest {

    @Data
    public class SimpleEntity implements Identifiable {

        private String id;
        private String theString;
        private String theOtherString;
        private Date theDate;

        public SimpleEntity(String string, String string2, Integer year) {
            this.theString = string;
            this.theOtherString = string2;
            this.theDate = Date.from(LocalDate.now().withYear(year).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private List<SimpleEntity> entityList;

    private Map<String, FieldFacet> facets;

    private Map<String, String> facetsConfig;

    @Before
    public void before() {
        entityList = new ArrayList<>();
        entityList.add(new SimpleEntity("a","A", 2016));
        entityList.add(new SimpleEntity("b","B", 2017));
        entityList.add(new SimpleEntity("c","C", 2018));

        facets = new HashMap<>();
        facetsConfig = new HashMap<>();
    }

    @Test
    @Ignore
    public void integer_as_filter_expression_is_invalid() {
        Filter Filter = new Filter("(17)");
        assertThat(Filter.isValid(),is(false));
    }

    @Test
    @Ignore
    public void empty_filter_expression_is_not_valid() {
        Filter Filter = new Filter("");
        assertThat(Filter.isValid(),is(false));
    }

    @Test
    public void simple_filter_expression_is_valid() {
        Filter filter = new Filter("(a=b)");
        assertThat(filter.isValid(),is(true));
        assertThat(filter.getPreparedStatement(),equalTo("a=:a"));
        assertThat(filter.getParams().size(),is(1));
        assertThat(filter.getParams().get("a"),is(equalTo("b")));
    }

    @Test
    public void simple_filter_expression_can_be_applied_to_entity() {
        Filter filter = new Filter("(theString=b)");
        List<SimpleEntity> filtered = applyFilter(filter, Collections.emptyMap());
        assertThat(filtered.size(),is(1));
        assertThat(filtered.get(0).getTheString(),is("b"));
    }

    @Test
    public void simple_filter_expression_with_yearFacet_can_be_applied_to_entities_date_field() {
        Filter filter = new Filter("(theDate;YYYY=2018)");
        facets.put("theDate", new YearFacet("id", facetsConfig));

        List<SimpleEntity> filtered = applyFilter(filter, facets);

        assertThat(filtered.size(),is(1));
        assertThat(filtered.get(0).getTheString(),is("c"));
    }

    @Test
    public void and_filter_expression_is_valid() {
        Filter filter = new Filter("(&(a=b)(c=d))");
        assertThat(filter.isValid(),is(true));
        assertThat(filter.getPreparedStatement(),equalTo("a=:a AND c=:c"));
        assertThat(filter.getParams().size(),is(2));
        assertThat(filter.getParams().get("a"),is(equalTo("b")));
        assertThat(filter.getParams().get("c"),is(equalTo("d")));
    }

    @Test
    public void and_filter_expression_can_be_applied_to_entity() {
        Filter filter = new Filter("(&(theString=a)(theOtherString=A))");

        List<SimpleEntity> filtered = applyFilter(filter, facets);

        assertThat(filtered.size(),is(1));
        assertThat(filtered.get(0).getTheString(),is("a"));
    }

    @Test
    public void and_filter_expression_with_three_arguments_is_valid() {
        Filter Filter = new Filter("(&(a=b)(c=d)(e=f))");
        assertThat(Filter.isValid(),is(true));
        assertThat(Filter.getPreparedStatement(),equalTo("a=:a AND c=:c AND e=:e"));
    }

    @Test
    public void or_filter_expression_is_valid() {
        Filter Filter = new Filter("(|(a=b)(c=d))");
        assertThat(Filter.isValid(),is(true));
        assertThat(Filter.getPreparedStatement(),equalTo("a=:a OR c=:c"));
    }

    @Test
    public void or_filter_expression_can_be_applied_to_entity() {
        Filter filter = new Filter("(|(theString=b)(theOtherString=C))");

        List<SimpleEntity> filtered = applyFilter(filter, facets);

        assertThat(filtered.size(),is(2));
        assertThat(filtered.get(0).getTheString(),is("b"));
        assertThat(filtered.get(1).getTheString(),is("c"));
    }

    @Test
    @Ignore // not implemented yet
    public void wildcard_filter_expression_is_valid() {
        Filter Filter = new Filter("(a=*b*))");
        assertThat(Filter.isValid(),is(true));
        assertThat(Filter.getPreparedStatement(),equalTo("a LIKE '%b%'"));
    }

    @Test
    public void not_filter_expression_is_valid() {
        Filter filter = new Filter("(!(a=b))");
        assertThat(filter.isValid(),is(true));
        assertThat(filter.getPreparedStatement(),equalTo("NOT (a=:a)"));
        assertThat(filter.getParams().size(),is(1));
        assertThat(filter.getParams().get("a"),is(equalTo("b")));
    }

    @Test
    public void not_filter_expression_can_be_applied_to_entity() {
        Filter filter = new Filter("(!(theString=b))");

        List<SimpleEntity> filtered = applyFilter(filter, facets);

        assertThat(filtered.size(),is(2));
        assertThat(filtered.get(0).getTheString(),is("a"));
        assertThat(filtered.get(1).getTheString(),is("c"));
    }

    @Test
    public void smaller_method_is_valid_expression() throws Exception {
        Filter filter = new Filter("(due < date())");
        assertThat(filter.isValid(),is(true));
        assertThat(filter.getPreparedStatement(),equalTo("due < date()"));
        assertThat(filter.getParams().size(),is(0));
    }

    @Test
    public void complex_smaller_is_valid() {
        Filter filter = new Filter("(&(due < date())(!(status=ARCHIVED)))");
        assertThat(filter.isValid(),is(true));
        assertThat(filter.getPreparedStatement(),equalTo("due < date() AND NOT (status=:status)"));
        assertThat(filter.getParams().size(),is(1));
        assertThat(filter.getParams().get("status"),is(equalTo("ARCHIVED")));
    }

    @Test
    public void element_of_out_edge_is_valid_expression() {
        Filter filter = new Filter("(#17:0 § out['parent'])");
        assertThat(filter.isValid(),is(true));
        assertThat(filter.getPreparedStatement(),equalTo("#17:0 IN out('parent')"));
        assertThat(filter.getParams().size(),is(0));
    }

    @Test
    public void element_of_in_edge_is_valid_expression() {
        Filter filter = new Filter("(#17:0 § in['todos'])");
        assertThat(filter.isValid(),is(true));
        assertThat(filter.getPreparedStatement(),equalTo("#17:0 IN in('todos')"));
        assertThat(filter.getParams().size(),is(0));
    }

    private List<SimpleEntity> applyFilter(Filter filter, Map<String, FieldFacet> facets) {
        List<SimpleEntity> filtered = entityList.stream().filter(t -> {
             return filter.evaluateEntity(t,SimpleEntity.class, facets);
        }).collect(Collectors.toList());
        return filtered;
    }


}
