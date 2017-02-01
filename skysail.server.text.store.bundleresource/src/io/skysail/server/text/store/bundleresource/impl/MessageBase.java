package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.forms.PostView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString(of = {"msgKey", "store"})
public class MessageBase implements Entity {

    @Field(inputType = InputType.READONLY)
    @PostView(tab = "Info")
    @Setter
    protected String msgKey;

    @Field(inputType = InputType.READONLY)
    @PostView(tab = "Info")
    @Setter
    private String store;

    @Field(inputType = InputType.HIDDEN)
    @Setter
    private String redirectTo;

//    @JsonIgnore
//    private Splitter splitter = Splitter.on(".").trimResults();
//
//    @JsonIgnore
//    private TranslationRenderService preferredRenderer;

    public MessageBase(String msgKey) {
        this.msgKey = msgKey;
    }

    @Override
    public String getId() {
        return msgKey;
    }

}
