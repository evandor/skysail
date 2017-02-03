package io.skysail.server.app.designer.codegen;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.osgi.framework.Bundle;
import org.restlet.resource.Resource;
import org.stringtemplate.v4.STGroupDir;
import org.stringtemplate.v4.compiler.CompiledST;

import io.skysail.server.services.StringTemplateProvider;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import st4hidden.org.antlr.runtime.ANTLRInputStream;
import st4hidden.org.antlr.runtime.ANTLRStringStream;
import st4hidden.org.antlr.runtime.CharStream;

/**
 * Instead of a usual file system directory, the ST group files are read from the relevant
 * bundles: the bundle containing the resource and the fallback bundle "skysail.server.converter",
 * containing all the default templates.
 *
 */
@Slf4j
public class STGroupBundleDir extends STGroupDir {

	private static final String NAME_IS_NOT_SUPPOSED_TO_CONTAIN_A_DOT = "name is not supposed to contain a dot";
	private static final String FOUND_RESOURCE_LOG_MSG_TEMPLATE = "found resource in {}: {}";
	private static final String UTF8_ENCODING = "UTF-8";
	private static final char DELIMITER_START_CHAR = '$';
	private static final char DELIMITER_STOP_CHAR = '$';

	private String optionalResourceClassName; // e.g. io.skysail.server.app.resources.DefaultResource
	private String bundleSymbolicName;

	private List<StringTemplateProvider> templateProvider;

	@Getter
	private static Set<String> usedTemplates = new LinkedHashSet<>();

	static {
		//verbose = true; // NOSONAR
	}

	/**
	 * @param bundle the bundle containing the resource to be rendered
	 * @param resource
	 * @param resourcePath e.g. "/templates"
	 */
	public STGroupBundleDir(Bundle bundle, Resource resource, @NonNull String resourcePath, @NonNull List<StringTemplateProvider> templateProvider) {
		super(bundle.getResource(resourcePath), UTF8_ENCODING, DELIMITER_START_CHAR, DELIMITER_STOP_CHAR);

		//this.optionalResourceClassName = resource.getClass().getName();
		this.bundleSymbolicName = bundle.getSymbolicName();
		this.groupDirName = getGroupDirName(bundle, resourcePath); // e.g. "STGroupBundleDir: skysail.server - /templates"
		this.templateProvider = templateProvider;
	}

	public void addUsedTemplates(Set<String> list) {
		usedTemplates.addAll(list);
	}

	/**
	 * @param name an template identifier with exaclty one '/' at the beginning and no dots, e.g. "/index".
	 */
	@Override
	public CompiledST load(@NonNull String name) {
		Validate.isTrue(name.startsWith("/"), "name is supposed to start with '/'");
		Validate.isTrue(!name.contains("."), NAME_IS_NOT_SUPPOSED_TO_CONTAIN_A_DOT);
		//Validate.isTrue(!name.substring(1).contains("/"), "name must not contain another '/' char.");

		return checkForResourceLevelTemplate(name)
				.orElse(checkForProvidedTemplates(name)
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
		Validate.isTrue(!name.contains("."), NAME_IS_NOT_SUPPOSED_TO_CONTAIN_A_DOT);

		URL groupFileURL = determineGroupFileUrl(name);
		if (groupFileURL == null) {
			return Optional.empty();
		}
		String fileName = root + ("/" + name + ".stg").replace("//", "/");
		if (!exists(groupFileURL)) {
			return Optional.ofNullable(loadTemplateFile("/", name + ".st")); // load t.st file // NOSONAR
		}
		usedTemplates.add(bundleSymbolicName + ": " + groupFileURL.toString());
		try {
			loadGroupFile("/", fileName);
			log.debug(FOUND_RESOURCE_LOG_MSG_TEMPLATE, bundleSymbolicName, groupFileURL.toString());
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
			log.debug(FOUND_RESOURCE_LOG_MSG_TEMPLATE, bundleSymbolicName, f.toString());
			usedTemplates.add(bundleSymbolicName + ": " + f.toString());
		} catch (IOException ioe) { // NOSONAR
			log.trace("resource does not exist in {}: {}", bundleSymbolicName, f.toString());
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
			log.info(FOUND_RESOURCE_LOG_MSG_TEMPLATE, bundleSymbolicName, f.toString());
			usedTemplates.add(bundleSymbolicName + ": " + f.toString());
		} catch (IOException ioe) { // NOSONAR
			log.trace("resource does not exist in {}: {}", bundleSymbolicName, f.toString());
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
		Validate.isTrue(!name.contains("."), NAME_IS_NOT_SUPPOSED_TO_CONTAIN_A_DOT);

		String templateFileName = name + ".st";
		Optional<String> optionalTemplate = templateProvider.stream()
				.map(tp -> tp.getTemplates().get(templateFileName))
				.filter(t -> t != null).findFirst();
		if (optionalTemplate.isPresent()) {
			log.debug("found provided Template for name {}", name);

			CharStream charStream = new ANTLRStringStream(optionalTemplate.get());
			if (charStream instanceof ANTLRStringStream && charStream.getSourceName() == null) {
				((ANTLRStringStream)charStream).name = templateFileName;
			}
			return Optional.ofNullable(loadTemplateFile("/", templateFileName, charStream));

		}
		return Optional.empty();
	}

	private Optional<CompiledST> checkForResourceLevelTemplate(String name) {
		if (optionalResourceClassName == null) {
			return Optional.empty();
		}
		String resourceLevelTemplate = (optionalResourceClassName + "Stg").replace(".", "/") + "/" + name;
		Optional<CompiledST> optionalCompiledST = loadFromBundle(name, resourceLevelTemplate);
		if (optionalCompiledST.isPresent()) {
			log.debug("found resourceLevelTemplate for name {}", name);
		}
		return optionalCompiledST;
	}

	private String getGroupDirName(Bundle bundle, String resourcePath) {
		return new StringBuilder(getClass().getSimpleName()).append(": ").append(bundle.getSymbolicName())
				.append(" - ").append(resourcePath).toString();
	}

}
