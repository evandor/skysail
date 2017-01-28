package io.skysail.server.queryfilter.sorting;

import java.util.Comparator;

import org.restlet.Request;

import io.skysail.domain.Entity;
import io.skysail.server.utils.params.SortingParamUtils;

public class Sorting {

    private Request request;

    public Sorting(Request request) {
        this.request = request;
    }

    public String getOrderBy() {
        return new SortingParamUtils("name", request).getOrderByStatement();
    }

    public Comparator<? super Entity> getComparator(Class<?> cls) {
        return new SortingParamUtils("name", request).getComparator(cls);
    }

}
