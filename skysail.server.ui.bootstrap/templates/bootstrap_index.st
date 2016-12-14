bootstrap_index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
    $head/bootstrap_head()$
  </head>
  
  <body>
    $nav/bootstrap_navbar(user, messages, converter, model)$
    $breadcrumbs/bootstrap_breadcrumbsRow(model,user)$

    $bootstrap_main(user, converter, messages, model)$
      
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

  </body>
</html>
>>