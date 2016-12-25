sui_renderTemplateProvider(styling) ::= <<
	$styling: {style | 
		$if(style.selected)$
    	  <a class="item" href='#'><b>$style.label$</b></a>
		$else$
	      <a class="item" href='?_styling=$style.shortName$'>$style.label$</a>
	    $endif$
    }$	
>>
