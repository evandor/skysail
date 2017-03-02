package io.skysail.app.spotify.services;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.restlet.Context;
import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.engine.util.Base64;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.app.spotify.SpotifyApplication;
import io.skysail.app.spotify.config.SpotifyConfiguration;
import io.skysail.app.spotify.domain.OAuthCallbackData;
import io.skysail.ext.oauth2.OAuth2Proxy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(immediate = true, service = ApiServices.class)
public class ApiServices {

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    @Getter
    private SpotifyConfiguration config;

    private static ObjectMapper mapper = new ObjectMapper();

    private static Map<String, OAuthCallbackData> accessData = new HashMap<>();

    public static void setAccessData(Principal principal, String text) {
        try {
            OAuthCallbackData readValue = mapper.readValue(text, OAuthCallbackData.class);
            accessData.put(principal.getName(), readValue);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static OAuthCallbackData getAccessData(Principal principal) {
        return accessData.get(principal.getName());
    }

    public void authorize(Context context, Response response) {

        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString(16).substring(0, 16);
        response.getCookieSettings().add(SpotifyApplication.SPOTIFY_AUTH_STATE, state);

        context.getAttributes().put(SpotifyApplication.SPOTIFY_AUTH_STATE, state);

        try {
            StringBuilder sb = new StringBuilder("https://accounts.spotify.com/authorize?");
            sb.append("response_type=").append("code");
            sb.append("&client_id=").append(config.getConfig().clientId());
            sb.append("&scope=").append("user-read-private%20user-read-email%20playlist-read-private");
            sb.append("&redirect_uri=" + URLEncoder.encode(config.getConfig().redirectUri(), "UTF-8"));
            sb.append("&state=").append(state);
            String query = sb.toString();
            log.info("querying {}", query);
            URL spotifyLoginUrl = new URL(query);
            org.restlet.data.Reference redirectTo = new org.restlet.data.Reference(spotifyLoginUrl);
            response.redirectSeeOther(redirectTo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public String getToken(String code) {
        StringBuilder sb = new StringBuilder("https://accounts.spotify.com/api/token");
        ClientResource cr = new ClientResource(sb.toString());

        String encoded = Base64
                .encode((config.getConfig().clientId() + ":" + config.getConfig().clientSecret()).getBytes(), false);

        ChallengeResponse challengeResponse = new ChallengeResponse(new ChallengeScheme("", ""));
        challengeResponse.setRawValue("Basic " + encoded);
        cr.setChallengeResponse(challengeResponse);

        log.info("Authorization set to '{}'", "Basic " + encoded);

        cr.setMethod(Method.POST);
        try {
            Form form = new Form();
            form.add("code", code);
            form.add("redirect_uri", URLEncoder.encode(config.getConfig().redirectUri(), "UTF-8"));
            form.add("grant_type", "authorization_code");
            log.info("form: {}", form);
            Representation repr = form.getWebRepresentation();
            repr.setCharacterSet(null);
            Representation posted = cr.post(repr, MediaType.APPLICATION_JSON);
            return posted.getText();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public String getMe(Principal principal) {
        OAuthCallbackData callbackData = getAccessData(principal);

        StringBuilder sb = new StringBuilder("https://api.spotify.com/v1/me");
        ClientResource cr = new ClientResource(sb.toString());

        ChallengeResponse challengeResponse = new ChallengeResponse(new ChallengeScheme("", ""));
        challengeResponse.setRawValue("Bearer " + callbackData.getAccessToken());
        cr.setChallengeResponse(challengeResponse);

        cr.setMethod(Method.GET);
        try {
            Representation posted = cr.get(MediaType.APPLICATION_JSON);
            return posted.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public String getPlaylists(Principal principal, Response response) {
        StringBuilder sb = new StringBuilder("https://api.spotify.com/v1/me/playlists");
        ClientResource cr = new ClientResource(sb.toString());

        ChallengeResponse challengeResponse = new ChallengeResponse(new ChallengeScheme("", ""));
        String accessToken = OAuth2Proxy.getAccessToken(principal).get();
        challengeResponse.setRawValue("Bearer " + accessToken);
        cr.setChallengeResponse(challengeResponse);

        cr.setMethod(Method.GET);
        Representation posted = cr.get(MediaType.APPLICATION_JSON);
        try {
            return posted.getText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
