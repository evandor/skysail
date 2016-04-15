package io.skysail.api.text;

import aQute.bnd.annotation.ConsumerType;

@ConsumerType
@FunctionalInterface
public interface I18nArgumentsProvider {

    MessageArguments getMessageArguments();

}
