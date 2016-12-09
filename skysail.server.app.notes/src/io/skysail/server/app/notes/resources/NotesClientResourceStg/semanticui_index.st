semanticui_index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
	<!-- Standard Meta -->
    <meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    
    <title>notes</title>
    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    
    <link rel="stylesheet" type="text/css" href="/_ui/semanticui/2.2.6/semantic.min.css">
	<script src="/_ui/semanticui/2.2.6/semantic.min.js"></script>
	
    <style type="text/css">
    	html, body {
		    height: 100%;
		}
		
		.ui.grid {
		    height: 100%;    
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
	<link rel="import" href="/_polymer/bower_components/sky-semanticui-list/sky-semanticui-list.html">
	
  	 
	<sky-menu prop1="notes"></sky-menu>
	
	<!--http://jsfiddle.net/ea04tkwo/-->
	<div class="ui padded equal height grid">
	  <div class="two wide column" style="border-right: 1px dotted grey; padding-top:55px;">
	    <h3>Categories</h3>
        <sky-semanticui-list url="/notes/v1/categories" selection="#renderTableDiv" id="notesDiv1" />
	  </div>
	  <div class="four wide stretched column" style="padding-top:55px;">
	    <div class="ui equal height grid">
	      <div class="sixteen wide column">
     	      <h3>Notes</h3>
	          <sky-load url="/notes/v1/notes" selection="#renderTableDiv" id="notesDiv" />
	      </div>
	    </div>
	  </div>
  	  <div class="ten wide column" style="border-left: 1px dotted grey; padding-top:55px;">
   	    <h3>Note</h3>
        <sky-load url="/notes/v1/notes/" selection="#showFormDiv" id="currentNote" />
	  </div>
	</div>
  </body>
</html>
>>