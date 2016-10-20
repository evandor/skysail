package io.skysail.server.queryfilter.sorting;

import org.restlet.Request;

import io.skysail.server.utils.ParamsUtils;

public class Sorting {

    private Request request;

    public Sorting(Request request) {
        this.request = request;
    }

    public String getOrderBy() {
        return ParamsUtils.getSorting(request);
    }

}
