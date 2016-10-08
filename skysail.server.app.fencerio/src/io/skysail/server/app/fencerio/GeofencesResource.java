package io.skysail.server.app.fencerio;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;

import io.skysail.server.restlet.resources.ListServerResource;

public class GeofencesResource extends ListServerResource<Geofence> {

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<?> getEntity() {
        URI uri; // https://
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");

         // set up a TrustManager that trusts everything
         sslContext.init(null, new TrustManager[] { new X509TrustManager() {
                     @Override
                    public X509Certificate[] getAcceptedIssuers() {
                             System.out.println("getAcceptedIssuers =============");
                             return null;
                     }

                     @Override
                    public void checkClientTrusted(X509Certificate[] certs,
                                     String authType) {
                             System.out.println("checkClientTrusted =============");
                     }

                     @Override
                    public void checkServerTrusted(X509Certificate[] certs,
                                     String authType) {
                             System.out.println("checkServerTrusted =============");
                     }
         } }, new SecureRandom());

            SSLSocketFactory sf = new SSLSocketFactory(sslContext);
            Scheme httpsScheme = new Scheme("https", 443, sf);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(httpsScheme);
            ClientConnectionManager cm = new BasicClientConnectionManager(schemeRegistry);
            HttpClient client = new DefaultHttpClient(cm);
            uri = new URIBuilder()
                    .setScheme("https")
                    .setHost("api.fencer.io")
                    .setPath("/v1.0/geofence")
                    .build();
            HttpGet httpget = new HttpGet(uri);
            httpget.addHeader(HttpHeaders.AUTHORIZATION,"7430de82-7013-5e77-88bb-fc138dd67a25");
            URI result = httpget.getURI();
            System.out.println(result);
            HttpResponse response = client.execute(httpget);
            System.out.println(response);
            Map<String,Object> responseMap = mapper.readValue(response.getEntity().getContent(), Map.class);
            System.out.println(responseMap);

            List<Geofence> geofences = new ArrayList<>();
            List<Map<String,?>> geos = (List<Map<String,?>>) responseMap.get("data");
            geos.stream().forEach(g -> geofences.add(new Geofence((String)g.get("id"), (String)g.get("alias"), (String)g.get("status"))));
            return geofences;

        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
