package io.skysail.api.text;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Translation {

    @Setter
    private String value;

    @Setter
    private String renderer;

    private TranslationStore store;
    
    private Collection<Object> messageArguments = Collections.emptyList();
    
    private Locale locale;

    public Translation(String text, TranslationStore store, Locale locale) {
    	this(text, store, locale, Collections.emptySet());
    }

    public Translation(String text, TranslationStore store, Locale locale, Collection<Object> messageArguments) {
        this.value = text;
        this.store = store;
        this.locale = locale;
        if (messageArguments != null) {
            this.messageArguments = messageArguments;
        }
    }

    public String getStoreName() {
        return store != null ? store.getClass().getSimpleName() : "-";
    }

}
