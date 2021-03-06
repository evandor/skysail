bst_index(user, messages, converter, model,request) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
  $head/bst_head()$
  </head>

  <body>
	<header>
	$nav/bst_navbar(user, messages, converter, model)$
	</header>
    
    <div class="container">
	  
	  <div class="row" style="height:27px;">
          <div class="col-md-2 no-float hidden-xs" style="background-color:#e1e1e1; border-right:1px dotted #bfbfbf;border-bottom:1px dotted #bfbfbf;">
        	  <div class="container" style="margin-top:0px;">
			      <div class="row" style="background-color:#e1e1e1;height:30px;">
				      <div class="col-md-12" style="margin-left:-15px;">
				          <h5 class="context-menu-appName"><b>$model.applicationName$</b></h5>
  				      </div>
				  </div>
		  	  </div>
          </div>
          <div class="col-md-10 no-float" style="background-color:#e1e1e1; border-bottom:1px dotted #bfbfbf;">
        	
          </div>
	  </div>
	  
	  <div class="row">
        <div class="col-md-12 no-float">
          $content/bst_content(user,converter,messages,model,request)$ 
        </div>
      </div>
	</div>
	
	<div class="container" style="margin-top:0px;height:40px;background-color:#e1e1e1;border-top:1px dotted #bfbfbf;padding-top:0px;">
	
	</div>
	
    $if(converter.debug && user.principal)$
      $debug(messages, model)$
    $endif$

    <script>
        function openWindow(location) {
	        window.open(location, '_blank');
        }

        function openLocation(location) {
	        window.location.href=location;
        }
    
		\$(function() {
		    \$.contextMenu({
		        selector: '.context-menu-appName',
				trigger: 'left',
		        items: {
		            "model":   {name: "show Model",   icon: "fa-book", callback: function() { openWindow('/$model.applicationName$/v1/_model');   } },
		            "swagger": {name: "swagger",      icon: "fa-eyedropper", callback: function() { openLocation('/$model.applicationName$/v1/_swagger?media=yaml');    } },
		            "i18n":    {name: "translations", icon: "fa-edit", callback: function() { openLocation('/$model.applicationName$/v1/_i18n'); } }
		        }
		    });
		    
		\$.contextMenu({
		        selector: '.context-menu-export',
		        trigger: 'left',
		        items: {
			        $model.representations:{ representation | $breadcrumbs/bst_representationType(representation,i)$}$
		        }
		    });

		});

        $converter.notifications: {notification | $notifications/bst_notify(notification)$}$

	</script>
		
  </body>
</html>
>>