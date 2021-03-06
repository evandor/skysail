package io.skysail.server.restlet.filter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.restlet.data.Header;
import org.restlet.util.Series;

import io.skysail.api.links.Link;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.core.utils.LinkUtils;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.response.Wrapper;
import io.skysail.server.utils.HeadersUtils;

public class AddLinkheadersFilter<R extends SkysailServerResource<T>, T extends Entity> extends AbstractResourceFilter<R, T> {

    private static final int MAX_LINK_HEADER_SIZE = 2048;

	@Override
    protected void afterHandle(R resource, Wrapper<T> responseWrapper) {
        if (resource instanceof SkysailServerResource) {
            SkysailServerResource<?> ssr = resource;
            Series<Header> responseHeaders = HeadersUtils.getHeaders(resource.getResponse());
            List<Link> linkheaderAuthorized = ssr.getAuthorizedLinks();
            linkheaderAuthorized.forEach(getPathSubstitutions(resource));
            String links = linkheaderAuthorized.stream().map(link -> link.toString(""))
                    .collect(Collectors.joining(","));
            Integer linkCount = 50;
            String limitedLinks = shrinkLinkHeaderSizeIfNecessary(linkCount, links);
            if (limitedLinks.length() < links.length()) {
            	responseHeaders.add(new Header("X-Link-Error", "link header was too large: " + links.length() + " bytes, cutting down to "+limitedLinks.length()+" bytes."));
            }
           	responseHeaders.add(new Header("Link", limitedLinks));
        }
    }

    private String shrinkLinkHeaderSizeIfNecessary(int linkCount, String links) {
    	if (linkCount <= 0) {
    		return "";
    	}
        if (links.length() > MAX_LINK_HEADER_SIZE) {
        	String reducedLinks = Arrays.stream(links.split(",",linkCount)).limit(linkCount-1).collect(Collectors.joining(",")); // NOSONAR
        	return shrinkLinkHeaderSizeIfNecessary(linkCount - 10, reducedLinks);
        }
       	return links;
	}

	/**
     * example: l -&gt; { l.substitute("spaceId", spaceId).substitute("id",
     * getData().getPage().getRid()); };
     * @param resource
     *
     * @return consumer for pathSubs
     */
    private Consumer<? super Link> getPathSubstitutions(R resource) {
        return l -> {
            String uri = l.getUri();
            l.setUri(LinkUtils.replaceValues(uri, resource.getRequestAttributes()));
        };
    }
}
