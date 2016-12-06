index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
	<!-- Standard Meta -->
    <meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    
    <title>notes</title>
    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    
    <!--<link rel="stylesheet" type="text/css" href="/_ui/semanticui/2.2.6/semantic.min.css">
	<script src="/_ui/semanticui/2.2.6/semantic.min.js"></script>-->
	
    <style type="text/css">
	  body {
	    background-color: #FFFFFF;
	  }
	  .ui.menu .item img.logo {
	    margin-right: 1.5em;
	  }
	  .main.container {
	    margin-top: 7em;
	  }
	  .wireframe {
	    margin-top: 2em;
	  }
	  .ui.footer.segment {
	    margin: 5em 0em 0em;
	    padding: 5em 0em;
	  }
    </style>
  </head>
  
  <body style="height:100%;">
	<base href="/_polymer/bower_components/app-layout/">
	
	<script src="../webcomponentsjs/webcomponents-lite.min.js"></script>

	<link rel="import" href="app-drawer/app-drawer.html">
	<link rel="import" href="app-header/app-header.html">
	<link rel="import" href="app-toolbar/app-toolbar.html">
	<link rel="import" href="demo/sample-content.html">
	<link rel="import" href="../iron-icons/iron-icons.html">
	<link rel="import" href="../paper-icon-button/paper-icon-button.html">
	<link rel="import" href="/_polymer/bower_components/iron-ajax/iron-ajax.html">
	<link rel="import" href="/_polymer/bower_components/test-el/test-el.html">
	<link rel="import" href="/_polymer/bower_components/sky-menu/sky-menu.html">
	<link rel="import" href="/_polymer/bower_components/sky-load/sky-load.html">
	
  	 
	<sky-menu prop1="notes"></sky-menu>
	
		<div class="ui grid" style="padding-top:54px;">
		  <div class="two wide column">2</div>
		  <div class="fourteen wide column">
		  	<sky-load url="/notes/v1/notes" selection="#renderTableDiv" /> 
		  </div>
		</div>
		<div class="ui grid">
		  <div class="sixteen wide column"></div>
		</div>
	

 <!-- <div class="ui vertical footer segment">
    <div class="ui center aligned container">
      <div class="ui stackable inverted divided grid">
        <div class="three wide column">
          <h4 class="ui inverted header">Group 1</h4>
          <div class="ui inverted link list">
            <a href="#" class="item">Link One</a>
          </div>
        </div>
        <div class="three wide column">
          <h4 class="ui inverted header">Group 2</h4>
          <div class="ui inverted link list">
            <a href="#" class="item">Link One</a>
          </div>
        </div>
        <div class="three wide column">
          <h4 class="ui inverted header">Group 3</h4>
          <div class="ui inverted link list">
            <a href="#" class="item">Link One</a>
          </div>
        </div>
        <div class="seven wide column">
          <h4 class="ui inverted header">Footer Header</h4>
          <p>Extra space for a call to action inside the footer that could help re-engage users.</p>
        </div>
      </div>
    </div>
  </div>-->
		
	
	
  </body>
</html>
>>