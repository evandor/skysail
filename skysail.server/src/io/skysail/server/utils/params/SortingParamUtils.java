package io.skysail.server.utils.params;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.utils.ParamsUtils;
import lombok.Getter;

public class SortingParamUtils extends ParamsUtils {

	private static final String SORTING_PARAM_KEY = "_s";

    private enum Direction {
        ASC("ASC"),
        DESC("DESC"),
        UNDEFINED(null);

        @Getter
        private String identifier;

        private Direction(String identifier) {
            this.identifier = identifier;
        }
    }

    public SortingParamUtils(String fieldname, Request request) {
		super(fieldname, request);
	}

    /**
     * Returns a query string for the provided field name, toggling between ASC,
     * DESC and empty state.
     *
     * The original request query is preserved.
     *
     * @param request
     * @param fieldname
     * @return
     */
    public String toggleSortLink() {
    	return super.toggleLink();
    }

    @Override
    protected Form handleQueryForm(FieldFacet facet, String format, String value) {
        if (getSortingParam() == null) {
            return formWithNewSortingParam(getFieldname(), Direction.ASC, cloneForm());
        }
        return updateParamInQueryForm();
    }

    @Override
    protected Form reduceQueryForm(FieldFacet facet, String format, String value) {
        return handleQueryForm(facet, format, value);
    }

    public String getOrderByStatement() {
        if (getSortingParam() == null) {
            return "";
        }
        Map<String, String> searchParams = getSearchParams(getSortingParam());
        String orderBy = searchParams.keySet().stream()
            .map(key -> key + " " + searchParams.get(key))
            .collect(Collectors.joining(","));
        return " order by " + orderBy;
    }

    @SuppressWarnings("unchecked")
    public Comparator<? super Identifiable> getComparator(Class<?> cls) {
        if (getSortingParam() == null) {
            return new Comparator<Identifiable>() {
                @Override
                public int compare(Identifiable o1, Identifiable o2) {
                    return 0;
                }
            };
        }
        Map<String, String> searchParams = getSearchParams(getSortingParam());
        String fieldkey = searchParams.keySet().iterator().next();

//        List list = new ArrayList(searchParams.keySet());
//        Collections.sort(list, Collections.reverseOrder());
//        Set reversedSet = new LinkedHashSet(list);

        List<Comparator> comparators = searchParams.keySet().stream().map(key -> {
            return new Comparator<Identifiable>() {
                @Override
                public int compare(Identifiable o1, Identifiable o2) {
                    try {
                        Field declaredField = cls.getDeclaredField(key);
                        declaredField.setAccessible(true);
                        Comparable object1 = (Comparable) declaredField.get(o1);
                        Comparable  object2 = (Comparable) declaredField.get(o2);
                        return searchParams.get(key).equals("ASC") ? object1.compareTo(object2) : object2.compareTo(object1);
                    } catch (Exception  e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            };
        })
        .map(Comparator.class::cast)
        .collect(Collectors.toList());

        return comparators.get(0);

//        return new Comparator<Identifiable>() {
//            @Override
//            public int compare(Identifiable o1, Identifiable o2) {
//                try {
//                    Field declaredField = cls.getDeclaredField(fieldkey);
//                    declaredField.setAccessible(true);
//                    Comparable object1 = (Comparable) declaredField.get(o1);
//                    Comparable  object2 = (Comparable) declaredField.get(o2);
//                    return searchParams.get(key).equals("ASC") ? object1.compareTo(object2) : object2.compareTo(object1);
//                } catch (Exception  e) {
//                    e.printStackTrace();
//                }
//                return 0;
//            }
//        };


//        String orderBy = searchParams.keySet().stream()
//            .map(key -> key + " " + searchParams.get(key))
//            .collect(Collectors.joining(","));
    }


    public String getSortIndicator() {
        if (getSortingParam() == null) {
            return "";
        }
        Map<String, String> searchParams = getSearchParams(getSortingParam());
        Direction ordering = Direction.valueOf(searchParams.get(getFieldname()));
        if (ordering == null) {
            return "";
        }
        if (Direction.ASC.equals(ordering)) {
            return "&nbsp;<small>&darr;</small>";
        }
        return "&nbsp;<small>&uarr;</small>";
    }
	private Form formWithNewSortingParam(String fieldname, Direction direction, Form queryForm) {
		queryForm.add(new Parameter(SORTING_PARAM_KEY, fieldname + ";" + direction.identifier));
		return queryForm;
	}

	private Form updateParamInQueryForm() {
        Map<String, String> searchParams = getSearchParams(getSortingParam());

        Form form = cloneForm();
        form.removeAll(SORTING_PARAM_KEY, true);

        Optional<String> keyForName = searchParams.keySet().stream().filter(key -> key.equals(getFieldname()))
                .findFirst();
        if (keyForName.isPresent()) {
            Direction direction = Direction.valueOf(searchParams.get(keyForName.get()));
            if (Direction.ASC.equals(direction)) {
                searchParams.put(keyForName.get(), Direction.DESC.identifier);
            } else {
                searchParams.remove(keyForName.get());
            }
        } else {
            searchParams.put(getFieldname(), Direction.ASC.identifier);
        }

        String newValue = getNewValue(searchParams);
        form.add(new Parameter(SORTING_PARAM_KEY, newValue));
        return form;
    }

	private Parameter getSortingParam() {
		return cloneForm().getFirst(SORTING_PARAM_KEY);
	}

    private String getNewValue(Map<String, String> searchParams) {
        String newValue = searchParams.keySet().stream()
                .map(key -> {
                    String paramValue = searchParams.get(key);
                    return key + ";" + paramValue;
                })
                .collect(Collectors.joining(","));
        return newValue;
    }

    private static Map<String, String> getSearchParams(Parameter found) {
        Map<String, String> searchParams = new HashMap<>();
        Arrays.stream(found.getValue().split(","))
                .map(expr -> expr.split(";"))
                .forEach(a -> {
                    searchParams.put(a[0], a[1]);
                });
        return searchParams;
    }

}
