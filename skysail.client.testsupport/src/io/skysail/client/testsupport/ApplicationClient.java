package io.skysail.client.testsupport;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.Header;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.engine.util.StringUtils;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.util.Series;

import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.authentication.AuthenticationStrategy;
import io.skysail.core.resources.SkysailServerResource;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationClient<T> {

    public static final String TESTTAG = " > TEST: ";

    private static final MediaType DEFAULT_ACCEPT_MEDIA_TYPE = MediaType.APPLICATION_JSON;

    @Getter
    private String baseUrl;
    private String credentials;
    private String url;
    private ClientResource cr;
    @Getter
    private Representation currentRepresentation;
    private String appName;

    @Getter
    private Series<Header> currentHeader;

	private ChallengeResponse challengeResponse;

    public ApplicationClient(@NonNull String baseUrl, @NonNull String appName) {
        this.baseUrl = baseUrl;
        this.appName = appName;
    }

    public ApplicationClient<T> setUrl(String url) {
        log.info("{}setting browser client url to '{}'", TESTTAG, url);
        this.url = url;
        return this;
    }

    public Representation get(MediaType mediaType) {
        String currentUrl = baseUrl + url;
        log.info("{}issuing GET on '{}', providing credentials {}", TESTTAG, currentUrl, credentials);
        cr = new ClientResource(currentUrl);
        //cr.setFollowingRedirects(false);
        if (!StringUtils.isNullOrEmpty(credentials)) {
        	cr.getCookies().add("Credentials", credentials);
        }
        cr.setChallengeResponse(challengeResponse);
        return cr.get(mediaType);
    }

    public ApplicationClient<T> gotoRoot() {
        url = "/";
        get(MediaType.APPLICATION_JSON);
        return this;
    }

    public ApplicationClient<T> gotoAppRoot() {
        return gotoAppRoot(DEFAULT_ACCEPT_MEDIA_TYPE);
    }

    public ApplicationClient<T> gotoAppRoot(MediaType mediaType) {
        gotoRoot().followLinkTitle(appName, mediaType);
        return this;
    }

    public ApplicationClient<T> gotoUrl(String relUrl,MediaType mediaType) {
        url = relUrl;
        currentRepresentation = get(mediaType);
        return this;
    }


    public Representation post(Object entity, MediaType mediaType) {
        log.info("{}issuing POST on '{}', providing credentials {}", TESTTAG, url, credentials);
        url = (url.contains("?") ? url + "&" : url + "?") + SkysailServerResource.NO_REDIRECTS ;
        cr = new ClientResource(url);
        cr.setFollowingRedirects(false);
        cr.getCookies().add("Credentials", credentials);
        cr.setChallengeResponse(challengeResponse);
        
		cr.setMethod(Method.POST);
        cr.getReference().addQueryParameter("format", "json");

        return cr.post(entity, mediaType);
    }

    public Response getResponse() {
        return cr.getResponse();
    }

    public ApplicationClient<T> loginAs(AuthenticationStrategy authenticationStrategy, @NonNull String username, @NonNull String password) {
        cr = authenticationStrategy.login(this, username, password);
        challengeResponse = cr.getChallengeResponse();
        credentials = cr.getCookies().getFirstValue("Credentials");
        return this;
    }

    public ApplicationClient<T> followLinkTitle(String linkTitle, MediaType mediaType) {
        return follow(new LinkTitlePredicate(linkTitle, cr.getResponse().getHeaders()), mediaType);
    }

    public ApplicationClient<T> followLinkTitleAndRefId(String linkTitle, String refId) {
        Link example = new Link.Builder("").title(linkTitle).refId(refId).build();
        return follow(new LinkByExamplePredicate(example, cr.getResponse().getHeaders()), MediaType.APPLICATION_JSON);
    }

    public ApplicationClient<T> followLinkRelation(LinkRelation linkRelation) {
        return follow(new LinkRelationPredicate(linkRelation, cr.getResponse().getHeaders()), MediaType.APPLICATION_JSON);
    }

    public ApplicationClient<T> followLink(Method method) {
        return followLink(method, null);
    }

    public ApplicationClient<T> followLink(Method method, T entity) {
        return follow(new LinkMethodPredicate(method, cr.getResponse().getHeaders()), method, entity, MediaType.APPLICATION_JSON);
    }

    private ApplicationClient<T> follow(LinkPredicate predicate, Method method, T entity,MediaType mediaType) {
        currentHeader = cr.getResponse().getHeaders();
        String linkheader = currentHeader.getFirstValue("Link");
        if (linkheader == null) {
            throw new IllegalStateException("no link header found");
        }
        List<Link> links = Arrays.stream(linkheader.split(",")).map(l -> Link.valueOf(l)).collect(Collectors.toList());
        Link theLink = getTheOnlyLink(predicate, links);

        boolean isAbsolute = false;
        try {
            URI url2 = new URI(theLink.getUri());
            isAbsolute = url2.isAbsolute();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        url =  isAbsolute ? theLink.getUri() : baseUrl + theLink.getUri();
        cr = new ClientResource(url);
        cr.getCookies().add("Credentials", credentials);
        cr.setChallengeResponse(challengeResponse);

        if (method != null) {
            if (!(theLink.getVerbs().contains(method))) {
                throw new IllegalStateException("method " + method + " not eligible for link " + theLink);
            }
            if (Method.DELETE.equals(method)) {
                log.info("{}issuing DELETE on '{}', providing credentials {}", TESTTAG, url, credentials);
                currentRepresentation = cr.delete(mediaType);
            } else if (Method.POST.equals(method)) {
                log.info("{}issuing POST on '{}' with entity '{}', providing credentials {}", TESTTAG, url, entity, credentials);
                currentRepresentation = cr.post(entity, mediaType);
            } else if (Method.PUT.equals(method)) {
                log.info("{}issuing PUT on '{}' with entity '{}', providing credentials {}", TESTTAG, url, entity, credentials);
                currentRepresentation = cr.put(entity, mediaType);
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            currentRepresentation = cr.get(mediaType);
            //url = currentRepresentation.getLocationRef().toUri().toString();
        }
        return this;
    }

    private ApplicationClient<T> follow(LinkPredicate predicate, MediaType mediaType) {
        return follow(predicate, null, null, mediaType);
    }

    private Link getTheOnlyLink(LinkPredicate predicate, List<Link> links) {
        List<Link> filteredLinks = links.stream().filter(predicate).collect(Collectors.toList());
        if (filteredLinks.size() == 0) {
            throw new IllegalStateException("could not find link for predicate " + predicate);
        }
        if (filteredLinks.size() > 1) {
            throw new IllegalStateException("too many candidates found for predicate " + predicate);
        }
        Link theLink = filteredLinks.get(0);
        return theLink;
    }

    public Reference getLocation() {
        return cr.getLocationRef();
    }

    public void setUrlFromCurrentRepresentation() {
        url = currentRepresentation.getLocationRef().toUri().toString();
    }



}
