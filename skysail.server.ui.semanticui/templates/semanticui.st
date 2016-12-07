semanticui(user, messages, converter, model) ::= <<<!DOCTYPE html>
<html lang="en">
  <head>
    $head()$
  </head>
  
  <body>
    $navbar(user, messages, converter, model)$
    $mainContent(user, converter, messages, model)$
    $footer(converter)$
  </body>
</html>
>>