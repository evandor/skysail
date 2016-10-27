package io.skysail.server.utils.params;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

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
    	return super.toggleLink(request, fieldname);
    }
    
    @Override
    protected Form handleQueryForm() {
        if (getSortingParam() == null) {
            return formWithNewSortingParam(fieldname, Direction.ASC, getForm());
        }
        return updateParamInQueryForm();
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

    public String getSortIndicator() {
        if (getSortingParam() == null) {
            return "";
        }
        Map<String, String> searchParams = getSearchParams(getSortingParam());
        Direction ordering = Direction.valueOf(searchParams.get(fieldname));
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
        
        getForm().removeAll(SORTING_PARAM_KEY, true);
        
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
        getForm().add(new Parameter(SORTING_PARAM_KEY, newValue));
        return getForm();
    }

	private Parameter getSortingParam() {
		return getForm().getFirst(SORTING_PARAM_KEY);
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
