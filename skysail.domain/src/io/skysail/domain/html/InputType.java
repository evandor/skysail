package io.skysail.domain.html;

import lombok.Getter;

// http://www.wufoo.com/html5/
public enum InputType {
    // HTML input types
    TEXT("input"),
    PASSWORD("input"),
    CHECKBOX("input"),
    RADIO("input"),
    SUBMIT("input"),
    RESET("input"),
    FILE("input"),
    HIDDEN("input"),
    IMAGE("input"),
    BUTTON("input"),
    // textarea tag
    TEXTAREA("input"),
    TAGS("input"),
    READONLY("input"),
    EMAIL("input"),
    URL("input"),
    RANGE("input"),
    TEL("input"),
    SEARCH("input"),
    COLOR("input"),
    NUMBER("input"),
    MULTISELECT("input"),

    DATE("input"),
    DATETIME_LOCAL("datetime-local"),
    MONTH("input"),
    WEEK("input"),
    TIME("input"),

    MARKDOWN_EDITOR("input"),
    TRIX_EDITOR("input"),
    TABLE("input"),

    POLYMER
    ;

	@Getter
    private String tag;

    private InputType() {
        tag = this.name();
    }

    private InputType(String value) {
        this.tag = value;
    }
}
