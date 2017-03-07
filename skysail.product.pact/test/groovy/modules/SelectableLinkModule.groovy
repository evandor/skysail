package modules

import geb.Module

class SelectableLinkModule extends Module {
    boolean isSelected() {
        parent().hasClass("selected")
    }
}

class HighlightsModule extends Module {
    static content = {
        highlightsLink { text -> $("a", text: text).module(SelectableLinkModule) }
        jQueryLikeApi { highlightsLink("jQuery-like API") }
    }
}
