bst_index(user, messages, converter, model,request) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
    $head/bst_head()$
  </head>
  
  <body>
    $nav/bst_navbar(user, messages, converter, model)$
    $breadcrumbs/bst_breadcrumbsRow(model,user)$

    $bst_main(user, converter, messages, model, request)$
      

  </body>
</html>
>>