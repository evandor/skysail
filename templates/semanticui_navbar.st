semanticui_navbar(user, messages, converter, model) ::= <<


<div class="ui secondary pointing menu">
  <a class="active item">
    Home
  </a>
  <a class="item">
    Messages
  </a>
  <a class="item">
    Friends
  </a>
  <div class="right menu">
    <a class="ui item">
      Logout
    </a>
  </div>
</div>
<div class="ui segment">
  <p></p>
</div>



<nav class="navbar navbar-default navbar-fixed-top" id="navbarNav">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a href="/" class="navbar-brand"><img src="/webapp/img/skysail.logo.png" height="25px" style="padding-left:5px;position:relative;top:-4px;"></a>
      <a href="/" class="navbar-brand" style="padding-left:0px;padding-right:50px;">$model.productName$</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        $if (!user.demoUser)$
          $dropdownFrontendsNav(model.services)$
          $dropdownApplicationsNav(model.services)$
          <!--dropdownDesignerNav(model.services)-->
        $endif$
        <!-- $dropdownAdminNav(user, model.services)$-->
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
      $if(user.principal)$
      <form class="navbar-form navbar-left">
        <div class="form-group">
          <input type="text" class="form-control" placeholder="Search">
        </div>
        <button type="submit" class="btn btn-default">Submit</button>
      </form>
      $endif$
      <ul class="nav navbar-nav navbar-right">
        $if(user.principal && user.developer)$
        <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Themes <span class="caret"></span></a>
        <ul class="dropdown-menu" role="menu">
          <li><a href='?_theme=bootstrap'>Bootstrap</a></li>
          <li><a href='javascript:document.cookie="mainpage=polymer;path=/";window.location.reload(true)'>Polymer</a></li>
          <li><a href='?_theme=jquerymobile'>Mobile View</a></li>
          <li><a href='?_theme=spa'>Single Page App</a></li>
          <li class="divider"></li>
          
          </ul>
        </li>
        $endif$

        $if(user.principal && user.developer)$
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">View (advanced) <span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <li><a href='javascript:document.cookie="mode=debug;path=/";window.location.reload(true)'>Debug</a></li>
            <li><a href='javascript:document.cookie="mode=edit;path=/";window.location.reload(true)'>Edit</a></li>
            <li><a href='javascript:document.cookie="mode=default;path=/";window.location.reload(true)'>Simple</a></li>
            <li><a href='javascript:inspect()'>Inspect Page</a></li>
          </ul>
        </li>
        $endif$
        $if(user.principal)$
          <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">$user.username$ <font style="color:red">$user.backend$</font><span class="caret"></span></a>
            <ul class="dropdown-menu" role="menu">
              <li><a href='/_logout?targetUri=/'>Logout</a></li>
              <li><a href='/_profile'>Profile</a></li>
            </ul>
          </li>
        $else$
          <li><a href="/_login">Login</a></li>
        $endif$  
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>
>>
