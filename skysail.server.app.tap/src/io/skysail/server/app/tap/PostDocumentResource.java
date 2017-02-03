package io.skysail.server.app.tap;

import javax.annotation.Generated;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.ResourceContextId;

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.Parser;
import org.osgi.framework.Bundle;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
// @Slf4j
public class PostDocumentResource extends PostEntityServerResource<Document> {

	private TapApplication app;
	private DbRepository repository;

	public PostDocumentResource() {
		addToContext(ResourceContextId.LINK_TITLE, "Create new Document");
	}

	@Override
	protected void doInit() {
		app = (TapApplication) getApplication();
		repository = (DbRepository) app.getRepository();
	}

	@Override
	public Document createEntityTemplate() {
		return new Document();
	}

	@Override
	public void addEntity(Document entity) {
		Tika tika = new Tika();
		Bundle bundle = app.getApplication().getBundle();
		URL resource = bundle.getResource("text.txt");

		System.out.println(resource.toString());

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(resource.openConnection().getInputStream()));
			while (br.ready()) {
				System.out.println(br.readLine());
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TikaConfig defaultConfig = TikaConfig.getDefaultConfig();
		
		MediaType mimeType = MediaType.parse("text/plain");

		Parser tikaParser = TikaConfig.getDefaultConfig().getParser(mimeType);

		try (java.io.InputStream stream = resource.openConnection().getInputStream()) {
			String result = tika.parseToString(new URL("http://svn.apache.org/repos/asf/tika/trunk/tika-example/src/main/resources/org/apache/tika/example/test2.doc"));
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String id = repository.save(entity, app.getApplicationModel()).toString();
		entity.setId(id);
	}

	@Override
	public List<Link> getLinks() {
		return super.getLinks(PostDocumentResource.class, DocumentsResource.class);
	}

	@Override
	public String redirectTo() {
		return super.redirectTo(DocumentsResource.class);
	}
}