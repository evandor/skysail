package io.skysail.server.queryfilter.sorting;

import java.util.Comparator;

import org.restlet.Request;

import io.skysail.domain.Identifiable;
import io.skysail.server.utils.params.SortingParamUtils;

public class Sorting {

    private Request request;

    public Sorting(Request request) {
        this.request = request;
    }

    public String getOrderBy() {
        return new SortingParamUtils("name", request).getOrderByStatement();
    }

    public Comparator<? super Identifiable> getComparator(Class<?> cls) {
        return new SortingParamUtils("name", request).getComparator(cls);
    }

}
