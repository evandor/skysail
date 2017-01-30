bst_navbar(user, messages, converter, model) ::= <<
  <nav class="navbar navbar-inverse navbar-fixed-top" id="navbarNav">
    <div class="container-fluid">
      <div class="navbar-header" style="width:264px;">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <!--<a href="/" class="navbar-brand"><img src="/webapp/img/skysail.logo.png" height="25px" style="padding-left:5px;position:relative;top:-4px;"></a>-->
        <a href="/" class="navbar-brand" style="padding-left:20px;padding-right:50px;">$model.productName$</a>
      </div>

      <!-- Collect the nav links, forms, and other content for toggling -->
      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="padding-left:281px;">

	  $nav/bst_dropdown("Applications", "play-circle-o", "", "4", model.applications)$

        <ul class="nav navbar-nav">
          $if(converter.debug && user.developer)$
          <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Debug <span class="caret"></span></a>
            <ul class="dropdown-menu" role="menu" style="width:350px;">
              <li><a href='#'>Status $model.status$</a></li>
              <li class="divider"></li>
              <li><a href='$model.entityTypeGithubLink$' target="_blank">Entity: $model.entityType$</a></li>
              <li><a href='#' data-toggle="modal" data-target="#linksModal">Links</a></li>
              <li><a href='#' data-toggle="modal" data-target="#templateModal">Templates</a></li>
              <li><a href='#' data-toggle="modal" data-target="#routesModal">Application Routes</a></li>
              <li><a href='#' data-toggle="modal" data-target="#appModelModal">Application Model</a></li>
              <li><a href='#' data-toggle="modal" data-target="#resourceModelModal">Resource Model ($model.resourceSimpleName$)</a></li>
              <li><a href='#' data-toggle="modal" data-target="#messagesModal">Messages</a></li>
              <li class="divider"></li>
              <li><a href='#'>$messages.("productName").value$ [$messages.("productVersion").value$]</a>
            </ul>
          </li>
          $endif$
        </ul>

        $if(user.principalXXX)$
        <form class="navbar-form navbar-left">
          <div class="form-group">
            <input type="text" class="form-control" placeholder="Search">
          </div>
          <button type="submit" class="btn btn-default">Submit</button>
        </form>
        $endif$
 
        $nav/bst_dropdown(user.username, "user", "navbar-right", "1", model.usernav)$
        $if(user.principal && user.developer)$
          $nav/bst_dropdown("View (Developer)", "", "navbar-right", "1", model.viewModes)$
        $endif$
      
        $nav/bst_dropdown("Styling", "", "navbar-right", "1", model.styling2)$

        $nav/bst_dropdown("Administration", "", "navbar-right", "1", model.adminMenu)$

      </div>
    </div>
  </nav>
>>
