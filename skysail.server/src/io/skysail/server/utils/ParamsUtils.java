package io.skysail.server.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParamsUtils {

    private static final String SEARCH_PARAM_KEY = "_s";

    /**
     * Returns a query string for the provided fieldname, toggling between ASC,
     * DESC and empty state.
     *
     * The original request query is preserved.
     *
     * @param request
     * @param fieldname
     * @return
     */
    public static String toggleSortLink(Request request, String fieldname) {
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
        queryForm.add(new Parameter(SEARCH_PARAM_KEY, fieldname + ";ASC"));
        return queryForm;
    }

    private static Form addNewSearchQuery(String fieldname, Form queryForm, Parameter found) {
        queryForm.removeAll(SEARCH_PARAM_KEY, true);
        Map<String, String> searchParams = getSearchParams(found);

        Optional<String> keyForName = searchParams.keySet().stream().filter(key -> key.equals(fieldname))
                .findFirst();
        if (keyForName.isPresent()) {
            String direction = searchParams.get(keyForName.get());
            if ("ASC".equals(direction)) {
                searchParams.put(keyForName.get(), "DESC");
            } else {
                searchParams.remove(keyForName.get());
            }
        } else {
            searchParams.put(fieldname, "ASC");
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
        String ordering = searchParams.get(name);
        if (ordering == null) {
            return "";
        }
        if ("ASC".equals(ordering)) {
            return "&nbsp;<small>&darr;</small>";
        }
        return "&nbsp;<small>&uarr;</small>";
    }

    private static String newSearchQuery(String fieldname) {
        Form queryForm = new Form();
        queryForm.add(new Parameter(SEARCH_PARAM_KEY, fieldname + ";ASC"));
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

    private static String emptyQueryRef(Request request) {
        return request.getOriginalRef().getHierarchicalPart();
    }

    private static boolean isEmpty(Form queryForm) {
        return "".equals(queryForm.getQueryString());
    }




}
