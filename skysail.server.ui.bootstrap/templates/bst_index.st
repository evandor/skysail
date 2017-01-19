bst_index(user, messages, converter, model,request) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
    $head/bst_head()$
  </head>
  
  <body>
	
	<header style="height: 50px;">
	  $nav/bst_navbar(user, messages, converter, model)$
	</header>
    <div class="container">
	  <div class="row" style="height:30px;">
	        <div class="col-md-2 no-float hidden-xs" style="background-color:#f5f5f5; border-right:1px dotted #bfbfbf;border-bottom:1px dotted #bfbfbf;">
	        	<div class="container">
  					<div class="row" style="background-color:#f5f5f5;height:30px;">
					    <div class="col-md-12" style="margin-left:-15px;">
					    	<h5 class="context-menu-appName">$model.applicationName$</h5>
					    </div>
					</div>
				</div>
	        </div>
	        <div class="col-md-10 no-float" style="background-color:#f5f5f5; border-bottom:1px dotted #bfbfbf;">
	        	$breadcrumbs/bst_breadcrumbsRow(model,user)$
	        </div>
	  </div>
	  <div class="row">
	        <div class="col-md-2 no-float hidden-xs" style="background-color:#f5f5f5;border-right:1px dotted #bfbfbf;">
	        	<h3>Entities</h3>
		        $menuNav(model.collectionLinks)$
	        	<h3>Other</h3>
	        	<h3>Facets</h3>
	        	$model.facets.buckets.keys: {key | $listFacets(key,model.facets.buckets.(key))$};separator="<br>"$
	       </div>
	       <div class="col-md-10 no-float">
	        	$content/bst_content(user,converter,messages,model,request)$ 
	        </div>
      </div>
	</div>
	
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
	</script>

	
	
  </body>
</html>
>>