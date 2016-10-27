package io.skysail.server.utils.params;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;

import io.skysail.server.utils.ParamsUtils;
import lombok.Getter;

public class SortingParamUtils extends ParamsUtils {

    private static final String SEARCH_PARAM_KEY = "_s";

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
    public String toggleSortLink(Request request, String fieldname) {
        Reference originalRef = request.getOriginalRef();
        if (!originalRef.hasQuery()) {
            return newSearchQuery(fieldname);
        }

        Form queryForm = handleQueryForm(fieldname, originalRef.getQueryAsForm());
        if (isEmpty(queryForm)) {
            return emptyQueryRef(request);
        }
        queryForm = stripEmptyParams(queryForm);
        return isEmpty(queryForm) ? request.getOriginalRef().getHierarchicalPart() : "?" + queryForm.getQueryString();
    }

    private static Form handleQueryForm(String fieldname, Form queryForm) {
        Parameter found = queryForm.getFirst(SEARCH_PARAM_KEY);
        if (found != null) {
            return addNewSearchQuery(fieldname, queryForm, found);
        }
        queryForm.add(new Parameter(SEARCH_PARAM_KEY, fieldname + ";" + Direction.ASC.identifier));
        return queryForm;
    }

	private static Form addNewSearchQuery(String fieldname, Form queryForm, Parameter found) {
        queryForm.removeAll(SEARCH_PARAM_KEY, true);
        Map<String, String> searchParams = getSearchParams(found);

        Optional<String> keyForName = searchParams.keySet().stream().filter(key -> key.equals(fieldname))
                .findFirst();
        if (keyForName.isPresent()) {
            Direction direction = Direction.valueOf(searchParams.get(keyForName.get()));
            if (Direction.ASC.equals(direction)) {
                searchParams.put(keyForName.get(), Direction.DESC.identifier);
            } else {
                searchParams.remove(keyForName.get());
            }
        } else {
            searchParams.put(fieldname, Direction.ASC.identifier);
        }

        String newValue = getNewValue(searchParams);
        queryForm.add(new Parameter(SEARCH_PARAM_KEY, newValue));
        return queryForm;
    }

    public static String getSorting(Request request) {
        Form queryForm = request.getOriginalRef().getQueryAsForm();
        Parameter found = queryForm.getFirst(SEARCH_PARAM_KEY);
        if (found == null) {
            return "";
        }
        Map<String, String> searchParams = getSearchParams(found);
        String orderBy = searchParams.keySet().stream()
            .map(key -> key + " " + searchParams.get(key))
            .collect(Collectors.joining(","));
        return " order by " + orderBy;
    }

    public static String getSortIndicator(Request request, String name) {
//        Reference originalRef = request.getOriginalRef();
//        if (!originalRef.hasQuery()) {
//            return "";
//        }
        Form queryForm = request.getOriginalRef().getQueryAsForm();
        Parameter found = queryForm.getFirst(SEARCH_PARAM_KEY);
        if (found == null) {
            return "";
        }
        Map<String, String> searchParams = getSearchParams(found);
        Direction ordering = Direction.valueOf(searchParams.get(name));
        if (ordering == null) {
            return "";
        }
        if (Direction.ASC.equals(ordering)) {
            return "&nbsp;<small>&darr;</small>";
        }
        return "&nbsp;<small>&uarr;</small>";
    }

    private static String newSearchQuery(String fieldname) {
        Form queryForm = new Form();
        queryForm.add(new Parameter(SEARCH_PARAM_KEY, fieldname + ";" + Direction.ASC.identifier));
        return "?" + queryForm.getQueryString();
    }

    private static Form stripEmptyParams(Form queryForm) {
        Form result = new Form();
        queryForm.forEach(param -> {
            if (param.getValue() != null && !"".equals(param.getValue().trim())) {
                result.add(param);
            }
        });
        return result;
    }

    private static String getNewValue(Map<String, String> searchParams) {
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
