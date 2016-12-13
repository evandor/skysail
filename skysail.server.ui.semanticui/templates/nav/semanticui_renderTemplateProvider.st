semanticui_renderTemplateProvider(styling) ::= <<
	$styling: {style | <a class="item" href='?_styling=$style.name$'>$if(style.selected)$* $endif$$style.label$</a>}$
>>
