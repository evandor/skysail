bst_renderTemplateProvider(styling) ::= <<
	$styling: {style | 
		$if(style.selected)$
			<li><a href='#'><b>$style.label$</b></a></li>
		$else$
			<li><a href='?_styling=$style.shortName$'>$style.label$</a></li>
		$endif$		
	}$
>>
