package io.skysail.server.queryfilter.pagination;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Header;
import org.restlet.util.Series;

import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.CookiesUtils;
import io.skysail.server.utils.HeadersUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class Pagination {

    private static final long DEFAULT_LINES_PER_PAGE = 20;

    @Getter
    private int page = 1;

    private Request request;

    private Response response;

    public Pagination(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public void setEntityCount(long entityCount) {
        Series<Header> headers = HeadersUtils.getHeaders(this.response);

        headers.add(new Header(HeadersUtils.PAGINATION_PAGES, getPagesCount(entityCount)));
        headers.add(new Header(HeadersUtils.PAGINATION_HITS, Long.toString(entityCount)));

        String pageQueryParameter = (String) request.getAttributes().get(SkysailServerResource.PAGE_PARAM_NAME);
        if (pageQueryParameter == null || pageQueryParameter.trim().length() == 0) {
            return;
        }
        try {
            page = Integer.parseInt(pageQueryParameter);
            headers.add(new Header(HeadersUtils.PAGINATION_PAGE, Integer.toString(page)));
        } catch (NumberFormatException e) {
            log.debug(e.getMessage(), e);
        }

    }

    private String getPagesCount(long entityCount) {
        return Long.toString(1 + Math.floorDiv(entityCount, 1 + DEFAULT_LINES_PER_PAGE));
    }

    public long getLinesPerPage() {
        String entriesPerPage = CookiesUtils.getEntriesPerPageFromCookie(request);
        if (entriesPerPage != null) {
            try {
                return Long.parseLong(entriesPerPage);
            } catch (NumberFormatException e) {
                log.info("could not parse {} as 'entriesPerPage' data", entriesPerPage);
            }
        }
        return DEFAULT_LINES_PER_PAGE;
    }

    public String getLimitClause() {
        long linesPerPage = getLinesPerPage();
        long page = getPage();
        if (linesPerPage <= 0) {
            return "";
        }
        return new StringBuilder("SKIP " + linesPerPage * (page-1) + " LIMIT " + linesPerPage).toString();
    }

}
