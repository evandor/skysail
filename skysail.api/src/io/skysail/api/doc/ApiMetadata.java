package io.skysail.api.doc;

import lombok.Builder;

@Builder
public class ApiMetadata {

  //  private Map<Method, Map<String, Object>> metadata = new HashMap<>();

    private ApiSummary getSummary;
    private ApiSummary postSummary;
    private ApiSummary putSummary;
    private ApiSummary deleteSummary;

    private ApiDescription getDescription;
    private ApiDescription postDescription;
    private ApiDescription putDescription;
    private ApiDescription deleteDescription;

//    public ApiMetadata addGetSummary(ApiSummary summary) {
//        if (summary == null || summary.value().trim().length() == 0) {
//            return this;
//        }
//
//
//        return this;
//    }
}
