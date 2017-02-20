package io.skysail.text.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.domain.html.InputType;
import io.skysail.server.forms.PostView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

//@Value
@Getter
@NoArgsConstructor
@ToString(callSuper = true, of = {"msg"})
public class Content implements Entity {

    @Field(inputType = InputType.TEXT)
    @PostView(tab = "Info")
    protected String id;

    @Field(htmlPolicy = HtmlPolicy.DEFAULT_HTML, inputType = InputType.TEXTAREA)
    @PostView(tab = "Content")
    private String msg;

    @Field(inputType = InputType.READONLY)
    @PostView(tab = "Info")
    private String store;

    @Field(inputType = InputType.HIDDEN)
    private String redirectTo;

    @JsonCreator
    public Content(
            @JsonProperty("id") String id,
            @JsonProperty("msg") String msg,
            @JsonProperty("store") String store,
            @JsonProperty("redirectTo") String redirectTo
            ) {
        this.id = id;
        this.msg = msg;
        this.store = store;
        this.redirectTo = redirectTo;
    }

    public Content withRedirect(String redirectBackToLocal) {
        return new Content(this.id, this.msg, this.store, redirectBackToLocal);
    }

}
