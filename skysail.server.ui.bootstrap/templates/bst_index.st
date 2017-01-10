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
	        <div class="col-md-2 no-float hidden-xs" style="background-color:#f5f5f5;height:30px; border-right:1px dotted #bfbfbf;border-bottom:1px dotted #bfbfbf;">
	        	<h4>CRM</h4>
	        </div>
	        <div class="col-md-10 no-float" style="background-color:#f5f5f5; border-bottom:1px dotted #bfbfbf;">
	        	$breadcrumbs/bst_breadcrumbsRow(model,user)$
	        </div>
	    </div>
	    <div class="row">
	        <div class="col-md-2 no-float hidden-xs" style="background-color:#f5f5f5;border-right:1px dotted #bfbfbf;">
	        	<h3>Navigation</h3>
		        <p>Content</p>
		        <p>Content</p>
		        <p>Content</p>
	        </div>
	        <div class="col-md-10 no-float">
	        	$content/bst_content(user,converter,messages,model,request)$ 
	        </div>
	    </div>
	</div>
  	
  </body>
</html>
>>