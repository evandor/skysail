semanticui_index(user, messages, converter, model) ::= <<<!DOCTYPE html>
<html lang="en">
  <head>
    $head/semanticui_head()$
  </head>
  
  <body>
    $nav/semanticui_navbar(user, messages, converter, model)$
  
    $semanticui_mainContent(user, converter, messages, model)$
    $semanticui_footer(converter)$
  </body>
</html>
>>