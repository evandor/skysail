index(user, messages, converter, model) ::= <<<!DOCTYPE html>
<html lang="en">
  <head>
    $head()$
  </head>
  
  <body>
   
  <div id="header">...</div>
  <script th:inline="javascript">
        \$("#header").load("http://localhost:2018/demoapp/v1/Bookmarks #renderTableDiv");
  </script>

  <div id="header2">...</div>
  <script th:inline="javascript">
        \$("#header2").load("http://localhost:2018/demoapp/v1/Bookmarks #renderTableDiv");
  </script>
  
  </body>
</html>
>>