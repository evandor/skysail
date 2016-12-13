semanticui_index(user, messages, converter, model) ::= <<<!DOCTYPE html>
<html lang="en">
  <head>
    $semanticui_head()$
  </head>
  
  <body>
    $semanticui_navbar(user, messages, converter, model)$
    mainContent(user, converter, messages, model)
    footer(converter)
  </body>
</html>
>>