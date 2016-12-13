bootstrap_index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
    $head/bootstrap_head()$
  </head>
  
  <body>
    $nav/bootstrap_navbar(user, messages, converter, model)$
    $breadcrumbs/bootstrap_breadcrumbsRow(model,user)$
    <div class="row main-row fill">
      <div class="col-sm-2 sidebar hidden-xs">
        <h3>Navigation</h3>
        <p>Content</p>
        <p>Content</p>
        <p>Content</p>
        <p>Content</p>
      </div>
      <div class="col-sm-10 col-xs-12 main-content-area">
        <h1 class="page-header">Content1</h1>
        <p>Content2</p>
      </div>
    </div>
      
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

  </body>
</html>
>>