package io.skysail.server.text.store.bundleresource.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Splitter;

import io.skysail.api.text.Translation;
import io.skysail.api.text.TranslationRenderService;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.server.forms.PostView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true, of = {"msg"})
@NoArgsConstructor
public class Message extends MessageBase {

    @Field(htmlPolicy = HtmlPolicy.DEFAULT_HTML)
    @PostView(tab = "Message")
    private String msg;

    @JsonIgnore
    private Splitter splitter = Splitter.on(".").trimResults();

    @JsonIgnore
    private TranslationRenderService preferredRenderer;

    public Message(String msgKey) {
        setMsgKey(msgKey);
    }

    public Message(String msgKey, String msg) {
        setMsgKey(msgKey);
        this.msg = msg.replace("'{'", "{").replace("'}'", "}");

    }

    public Message(String key, Translation translation, TranslationRenderService preferredRenderer) {
        this(key, translation.getValue());
        this.preferredRenderer = preferredRenderer;
        setStore(translation.getStore().getClass().getName());
        if (preferredRenderer != null) {
            this.msg = preferredRenderer.adjustText(this.msg);
        }
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStoreChooser() {
        return "";

    }

}
