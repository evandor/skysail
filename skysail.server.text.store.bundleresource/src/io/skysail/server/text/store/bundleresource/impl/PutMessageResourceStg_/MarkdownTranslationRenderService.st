MarkdownTranslationRenderService(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en-gb" dir="ltr">

  <head>
      <meta charset="utf-8">
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <meta name="description" content="">
      <meta name="author" content="">
      <title>Skysail Translations</title>
      <script type="text/javascript" src="/webapp/js/jquery-1.11.1.js"></script>
      <link href="/webapp/css/bootstrap.min.css" rel="stylesheet">
      <link href="/webapp/css/bootstrap-theme.min.css" rel="stylesheet">
      $endOfHeaderJS()$
  </head>

  <body role="document">
        
    <!-- Fixed navbar -->
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="/">Skysail Translations</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="#">$user.principal$</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>

    <br><br><br>
    
    <div class="container-fluid">
      <div class="col-md-1">&nbsp;</div>  
      <div class="col-md-10">
        <div class="row">
            $menuNav(model.links)$
        </div>
        <div class="row">
            &nbsp;
        </div>
        <div class="row">
              $contentHeader(messages)$
        </div>
        <div class="row">
          <hr>
          $translationHeader(model, "Markdown")$
          <hr>

          <form role="form" action=".?method=PUT&_redirectTo=$model.redirectBackTo$" method="POST">
            <div class="form-group">
                <textarea data-uk-htmleditor="{maxsplitsize:600, markdown:true}" name="msg">$first(model.data).msg$</textarea>
                <hr>
                <label for="exampleInputEmail1">Markdown!! [<a href='http://daringfireball.net/projects/markdown/syntax' target='_blank'">?</a>] for $first(model.data).msgKey$</label>
            </div>
            <button type="submit" class="btn btn-default">Update</button>
        </form>
    </div>
      
      <div class="col-md-1">&nbsp;</div>
    </div>
  
</html>

>>