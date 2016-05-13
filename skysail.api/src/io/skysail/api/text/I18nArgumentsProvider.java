package io.skysail.api.text;

import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
@FunctionalInterface
public interface I18nArgumentsProvider {

    MessageArguments getMessageArguments();

}
