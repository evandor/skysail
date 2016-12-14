sui_index(user, messages, converter, model) ::= <<<!DOCTYPE html>
<html lang="en">
  <head>
    $head/sui_head()$
  </head>
  
  <body>
    $nav/sui_navbar(user, messages, converter, model)$
  
    $sui_main(user, converter, messages, model)$
  </body>
</html>
>>