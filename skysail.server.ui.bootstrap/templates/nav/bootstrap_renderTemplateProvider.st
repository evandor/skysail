bootstrap_renderTemplateProvider(styling) ::= <<
	$styling: {style | <li><a href='?_styling=$style.name$'>$if(style.selected)$* $endif$$style.label$</a></li>}$
>>
