mdb_renderTemplateProvider(styling) ::= <<
	$styling: {style | <a class="dropdown-item" href='?_styling=$style.shortName$'>$if(style.selected)$<b>$endif$$style.label$$if(style.selected)$</b>$endif$</a>}$
>>
