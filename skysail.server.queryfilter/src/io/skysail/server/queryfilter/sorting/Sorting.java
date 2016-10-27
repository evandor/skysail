package io.skysail.server.queryfilter.sorting;

import org.restlet.Request;

import io.skysail.server.utils.params.SortingParamUtils;

public class Sorting {

    private Request request;

    public Sorting(Request request) {
        this.request = request;
    }

    public String getOrderBy() {
        return new SortingParamUtils("name", request).getOrderByStatement();
    }

}
