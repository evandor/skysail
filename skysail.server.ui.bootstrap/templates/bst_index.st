bst_index(user, messages, converter, model,request) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
    $head/bst_head()$
    
    <link rel="import" href="/_polymer/bower_components/carbon-i18n-behavior/carbon-i18n-behavior.html">
    <script>
	    !function() {
		    Polymer.CarbonI18nBehaviorLocales.add('sky-form', 'de', {
	    	    hello: 'Hallo Welt',
	        	members: '{0} Mitglieder',
	        	intro: 'Mein Name ist {0} und ich bin {1} Jahre alt.'
		    });
		}();
    </script>
  </head>
  
  <body>
    $nav/bst_navbar(user, messages, converter, model)$
    $breadcrumbs/bst_breadcrumbsRow(model,user)$

    $bst_main(user, converter, messages, model, request)$
      

  </body>
</html>
>>