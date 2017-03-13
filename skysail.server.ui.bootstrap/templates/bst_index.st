bst_index(user, messages, converter, model, request) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
  $head/bst_head(converter)$
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
				      <div class="col-md-16" style="margin-left:-15px;">
				          <h5 class="context-menu-appName" style="cursor:pointer"><b>$model.applicationName$</b></h5>
  				      </div>
				  </div>
		  	  </div>
          </div>
          <div class="col-md-14 no-float" style="background-color:#e1e1e1; border-bottom:1px dotted #bfbfbf;">
        	$breadcrumbs/bst_breadcrumbsRow(model,converter,user)$
          </div>
	  </div>
	  
	  <div class="row">
        <div class="col-md-2 no-float hidden-xs" style="background-color:#e1e1e1;border-right:1px dotted #bfbfbf;">
            $converter.leftNavPolymerExtensions: {extension | <$extension$ />};separator="\n"$
        	<h3></h3>
	        $menuNav(model.collectionLinks)$
        	<h3></h3>
        	<h3></h3>
        	$model.facets.buckets.keys: {key | $listFacets(key,model.facets.buckets.(key))$};separator="<br>"$
        </div>
        <div class="col-md-14 no-float">
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
		            "model":    {name: "show Model",     icon: "fa-book",       callback: function() { openWindow  ('/$model.applicationName$/v1/_model');   } },
		            "swagger1": {name: "swagger (json)", icon: "fa-eyedropper", callback: function() { openWindow  ('/_doc/swagger/2.0/api/$model.applicationName$/v1'); } },
		            "swagger2": {name: "swagger (UI)",   icon: "fa-eyedropper", callback: function() { openWindow  ('http://swagger.skysail.io?url=http://$model.host$/_doc/swagger/2.0/api/$model.applicationName$/v1'); } },
		            "i18n":     {name: "translations",   icon: "fa-edit",       callback: function() { openLocation('/$model.applicationName$/v1/_i18n'); } }
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