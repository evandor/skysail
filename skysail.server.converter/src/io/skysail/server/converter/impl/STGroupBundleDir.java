package io.skysail.server.converter.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.osgi.framework.Bundle;
import org.restlet.resource.Resource;
import org.stringtemplate.v4.STGroupDir;
import org.stringtemplate.v4.compiler.CompiledST;

import io.skysail.server.services.DefaultStringTemplateProvider;
import io.skysail.server.services.StringTemplateProvider;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import st4hidden.org.antlr.runtime.ANTLRInputStream;
import st4hidden.org.antlr.runtime.ANTLRStringStream;
import st4hidden.org.antlr.runtime.CharStream;

@Slf4j
public class STGroupBundleDir extends STGroupDir {

	private static final String FOUND_RESOURCE_LOG_MSG_TEMPLATE = "found resource in {}: {}";
	private static final String ENCODING = "UTF-8";
	private static final char DELIMITER_START_CHAR = '$';
	private static final char DELIMITER_STOP_CHAR = '$';
	
	private String optionalResourceClassName; // e.g. io.skysail.server.app.resources.DefaultResource
	private String bundleName;
	
	private List<StringTemplateProvider> templateProvider;

	@Getter
	private static Set<String> usedTemplates = new LinkedHashSet<>();

	static {
		//verbose = true; // NOSONAR
	}

	public STGroupBundleDir(@NonNull Bundle bundle, @NonNull String resourcePath) {
		this(bundle, null, resourcePath, new ArrayList<StringTemplateProvider>());
	}

	/**
	 * @param bundle
	 * @param resource
	 * @param resourcePath e.g. "/templates"
	 */
	public STGroupBundleDir(Bundle bundle, Resource resource, @NonNull String resourcePath, @NonNull List<StringTemplateProvider> templateProvider) {
		super(bundle.getResource(resourcePath), ENCODING, DELIMITER_START_CHAR, DELIMITER_STOP_CHAR);
		
		this.optionalResourceClassName = resource != null ? resource.getClass().getName() : null;
		this.bundleName = bundle.getSymbolicName();
		this.groupDirName = getGroupDirName(bundle, resourcePath); // e.g. "STGroupBundleDir: skysail.server - /templates"
		this.templateProvider = templateProvider;
	}

	public void addUsedTemplates(Set<String> list) {
		usedTemplates.addAll(list);
	}

	/**
	 * From parent: Load a template from directory or group file. Group file is
	 * given precedence over directory with same name. {@code name} is always
	 * fully-qualified.
	 */
	@Override
	public CompiledST load(@NonNull String name) {
		Validate.isTrue(name.startsWith("/"), "name is supposed to start with '/'");
		Validate.isTrue(!name.contains("."), "name is not supposed to contain a dot");
		Validate.isTrue(!name.substring(1).contains("/"), "name must not contain another '/' char.");

		return checkForProvidedTemplates(name)
				.orElse(checkForResourceLevelTemplate(name)
				.orElse(loadFromBundle(name, name)
				.orElse(null)));
	}

	/** Load .st as relative file name relative to root by prefix */
	@Override
	public CompiledST loadTemplateFile(String prefix, String fileName) {
		return loadFromUrl(prefix, fileName, getUrl(fileName));
	}

	public CompiledST loadHtmlTemplateFile(String prefix, String fileName) {
		return loadHtmlFromUrl(prefix, fileName, getUrl(fileName));
	}

	public static void clearUsedTemplates() {
		usedTemplates.clear();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(groupDirName);
		if (!getImportedGroups().isEmpty()) {
			sb.append(":\nimportedGroups:\n");
			sb.append(getImportedGroups()
					.stream()
					.map(g -> "  > ".concat(g.toString()))
					.collect(Collectors.joining("\n")));
		}
		return sb.toString();
	}

	private Optional<CompiledST> loadFromBundle(String originalName, String name) {
		Validate.isTrue(!name.contains("."), "name is not supposed to contain a dot");

		URL groupFileURL = determineGroupFileUrl(name);
		if (groupFileURL == null) {
			return Optional.empty();
		}
		String fileName = root + ("/" + name + ".stg").replace("//", "/");
		if (!exists(groupFileURL)) {
			return Optional.ofNullable(loadTemplateFile("/", name + ".st")); // load t.st file // NOSONAR
		}
		usedTemplates.add(bundleName + ": " + groupFileURL.toString());
		try {
			loadGroupFile("/", fileName);
			log.debug(FOUND_RESOURCE_LOG_MSG_TEMPLATE, bundleName, groupFileURL.toString());
			log.debug("");
			return Optional.ofNullable(rawGetTemplate(originalName));
		} catch (Exception e) { // NOSONAR
		}
		if (root != null && "/".trim().length() != 0) {
			loadGroupFile("/", fileName);
		}
		return Optional.ofNullable(rawGetTemplate(name));
	}

	private CompiledST loadFromUrl(String prefix, String fileName, URL f) {
		ANTLRInputStream fs;
		try {
			fs = new ANTLRInputStream(f.openStream(), encoding);
			fs.name = fileName;
			log.debug(FOUND_RESOURCE_LOG_MSG_TEMPLATE, bundleName, f.toString());
			usedTemplates.add(bundleName + ": " + f.toString());
		} catch (IOException ioe) {
			log.trace("resource does not exist in {}: {}", bundleName, f.toString());
			return null;
		}
		return loadTemplateFile(prefix, fileName, fs);
	}

	private CompiledST loadHtmlFromUrl(String prefix, String fileName, URL f) {
		ANTLRInputStream fs;
		try {
			Set<InputStream> streamSet = new LinkedHashSet<>();
			streamSet.add(new ByteArrayInputStream(
					"index(user, messages, converter, model) ::= <<".getBytes(StandardCharsets.UTF_8)));
			streamSet.add(f.openStream());
			streamSet.add(new ByteArrayInputStream(">>".getBytes(StandardCharsets.UTF_8)));
			SequenceInputStream sequenceInputStream = new SequenceInputStream(new Vector(streamSet).elements());

			fs = new ANTLRInputStream(sequenceInputStream, encoding);
			fs.name = fileName;
			log.info(FOUND_RESOURCE_LOG_MSG_TEMPLATE, bundleName, f.toString());
			usedTemplates.add(bundleName + ": " + f.toString());
		} catch (IOException ioe) {
			log.trace("resource does not exist in {}: {}", bundleName, f.toString());
			return null;
		}
		return loadTemplateFile(prefix, fileName, fs);
	}

	private URL getUrl(String fileName) {
		try {
			return new URL(root + ("/" + fileName).replace("//", "/"));
		} catch (MalformedURLException me) {
			log.error(root + fileName, me);
			return null;
		}
	}

	private static boolean exists(URL url) {
		try {
			url.openConnection().connect();
		} catch (IOException e) { // NOSONAR
			return false;
		}
		return true;
	}

	private URL determineGroupFileUrl(String name) {
		if (root == null) {
			return null;
		}
		String url = root + ("/" + name + ".stg").replace("//", "/");
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			errMgr.internalError(null, "bad URL: " + url, e);
		}
		return null;
	}
	private Optional<CompiledST> checkForProvidedTemplates(String name) {
		Validate.isTrue(!name.contains("."), "name is not supposed to contain a dot");
		
		Optional<String> optionalTemplate = templateProvider.stream().map(tp -> {
			return tp.getTemplates().get(name + ".st");
		}).filter(t -> t != null).findFirst();
		if (optionalTemplate.isPresent()) {
			CharStream charStream = new ANTLRStringStream(optionalTemplate.get());
			return Optional.ofNullable(loadTemplateFile("/", name + ".st", charStream));
			
		}
		return Optional.empty();
	}
	
	private Optional<CompiledST> checkForResourceLevelTemplate(String name) {
		if (optionalResourceClassName == null) {
			return Optional.empty();
		}
		String resourceLevelTemplate = (optionalResourceClassName + "Stg").replace(".", "/") + "/" + name;
		return loadFromBundle(name, resourceLevelTemplate);
	}
	
	private String getGroupDirName(Bundle bundle, String resourcePath) {
		return new StringBuilder(getClass().getSimpleName()).append(": ").append(bundle.getSymbolicName())
				.append(" - ").append(resourcePath).toString();
	}

}
