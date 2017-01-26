package io.skysail.server.text.store.bundleresource.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import io.skysail.api.text.Translation;
import io.skysail.api.text.TranslationRenderService;
import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.domain.html.InputType;
import io.skysail.server.forms.PostView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(of = {"msgKey", "msg", "store"})
@NoArgsConstructor
public class Message implements Identifiable {

    @Field(htmlPolicy = HtmlPolicy.DEFAULT_HTML)
    @PostView(tab = "Message")
    private String msg;

    @Field(inputType = InputType.READONLY)
    @PostView(tab = "Info")
    @Setter
    private String msgKey;

    @Field(inputType = InputType.READONLY)
    @PostView(tab = "Info")
    private String store;

    @Field(inputType = InputType.HIDDEN)
    @Setter
    private String redirectTo;

   // private Set<String> availableStores;

    @JsonIgnore
    private Splitter splitter = Splitter.on(".").trimResults();

    @JsonIgnore
    private TranslationRenderService preferredRenderer;

    public Message(String msgKey) {
        this.msgKey = msgKey;
    }

    public Message(String msgKey, String msg) {
        this.msgKey = msgKey;
        this.msg = msg.replace("'{'", "{").replace("'}'", "}");

    }

    public Message(String key, Translation translation, TranslationRenderService preferredRenderer) {
        this(key, translation.getValue());
        this.preferredRenderer = preferredRenderer;
        store = translation.getStore().getClass().getName();
        //this.availableStores = translation.getStores();
        if (preferredRenderer != null) {
            this.msg = preferredRenderer.adjustText(this.msg);
        }
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStoreChooser() {
        return "";
//        return availableStores.stream().map(s -> {
//            if (s.equals(store)) {
//                return "<u>" + simpleName(store) + "</u>";
//            }
//            return "<a href='?store="+s+"'>" + simpleName(s) + "</a>";
//        }).collect(Collectors.joining(" - "));

    }

    private String simpleName(String className) {
        return Iterables.getLast(splitter.split(className));
    }

    @Override
    public String getId() {
        return msgKey;
    }

    @Override
    public void setId(String id) {
        // do nothing
    }
}
