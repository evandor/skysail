package io.skysail.server.converter.wrapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.server.utils.params.SortingParamUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class STRequestWrapper {

    @Getter
    public class RequestAdapter {

        private String hierarchicalPart;
        
        private List<Parameter> query;
        
        @Setter
        Map<String, String> sortIndicators;

        @Setter
        Map<String, String> toggleSortLinks;

        private String context = "/";

        public RequestAdapter(Request request) {
            hierarchicalPart = request.getOriginalRef().getHierarchicalPart();
            query = Collections.unmodifiableList(request.getOriginalRef().getQueryAsForm());
            List<String> segments = request.getOriginalRef().getSegments();
            if (segments != null && segments.size() > 1) {
                context = "/" + segments.get(0) + "/" + segments.get(1);
            }
        }


    }

    private ObjectMapper mapper = new ObjectMapper();
    
    private RequestAdapter adapter;

    public STRequestWrapper(Request request, List<String> fieldNames) {
        this.adapter = new RequestAdapter(request);
        
        adapter.setSortIndicators(fieldNames.stream()
                .collect(Collectors.toMap(
                            Function.identity(),
                            f -> new SortingParamUtils(f, request).getSortIndicator()
                        )));
        adapter.setToggleSortLinks(fieldNames.stream()
                .collect(Collectors.toMap(
                            Function.identity(),
                            f -> new SortingParamUtils(f, request).toggleSortLink()
                        )));
        
    }

    public String getAsJson() { //
        try {
            return mapper.writeValueAsString(adapter);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return "[]";
        }
    }
}
