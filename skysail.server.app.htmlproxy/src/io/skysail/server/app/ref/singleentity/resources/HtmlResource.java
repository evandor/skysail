package io.skysail.server.app.ref.singleentity.resources;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.ref.singleentity.HtmlPage;
import io.skysail.server.app.ref.singleentity.HtmlProxyApplication;
import io.skysail.server.restlet.resources.EntityServerResource;

public class HtmlResource extends EntityServerResource<HtmlPage> {

	private String id;
	private HtmlProxyApplication app;

	@Override
	protected void doInit() {
		id = getAttribute("id");
		app = (HtmlProxyApplication) getApplication();
	}

	@Override
	public SkysailResponse<?> eraseEntity() {
		return new SkysailResponse<>();
	}

	@Override
	public HtmlPage getEntity() {

		String finalHTML = "";
		try {
			URL url = new URL("http://www.heise.de");
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.11.140", 8080));
			URLConnection spoof = url.openConnection(proxy);

			// Spoof the connection so we look like a web browser
			spoof.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0;    H010818)");
			BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
			String strLine = "";
			
			// Loop through every line in the source
			while ((strLine = in.readLine()) != null) {
				finalHTML += strLine;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HtmlPage(finalHTML);
		
	}

}
