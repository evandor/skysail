package io.skysail.api.text;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A Translation render service asks a TranslationStore for the translation
 * object for a given key and passes this to its render function to create the
 * translated value.
 *
 * Different implementations can post-process the retrieved translation, for
 * example they can provide asciidoc or markdown formatting.
 *
 */
@ProviderType
public interface TranslationRenderService {

    String render(String content, Object... substitutions);

    String render(Translation translation);

    boolean applicable(String unformattedTranslation);

    String adjustText(String unformatted);

    String addRendererInfo();

}
