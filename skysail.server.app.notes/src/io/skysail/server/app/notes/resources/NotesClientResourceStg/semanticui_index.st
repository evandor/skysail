semanticui_index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
	<!-- Standard Meta -->
    <meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    
    <title>notes</title>
    
    <script src="/webapp/js/moment.js"></script>
    
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
	
	<link rel="import" href="/_ui/semanticui/sky-semanticui/sky-list.html">
	<link rel="import" href="/_ui/semanticui/sky-semanticui/sky-entity.html">
	<link rel="import" href="/_ui/semanticui/sky-semanticui/sky-add-entity.html">
	
	<link rel="import" href="/notes/v1/sky-note.html">
	<link rel="import" href="/notes/v1/sky-new-note.html">
	
	
	<sky-menu prop1="notes"></sky-menu>
	
	<!--http://jsfiddle.net/ea04tkwo/-->
	<div class="ui padded equal height grid">
	  <div class="two wide column" style="border-right: 1px dotted grey; padding-top:55px;">
	    <h3>Categories1</h3>
	    
        <sky-list
        	url="/notes/v1/categories" 
        	link-template="/notes/v1?category={id}"
        	id="categoriesListDiv" 
        	icon="inbox"
        />
	  
	  </div>
	  <div class="four wide stretched column" style="padding-top:55px;">
	    <div class="ui equal height grid">
	      <div class="sixteen wide column">
     	      <h3>Notes</h3>

		      <sky-list
		        url="/notes/v1/notes" 
		      	title-column="title" 
		      	filter="(category=$model.params.("category")$)" 
            	target-template="/notes/v1/notes/{id}/"
		      	id="notesListDiv"
		      	icon="content-paste"
		      	clicked="replacetarget"
		      	tag="sky-note"
		      />
		      
	      </div>
	    </div>
	  </div>
  	  <div class="ten wide column" style="border-left: 1px dotted grey; padding-top:55px;">
   	    
   	    <h3>Note</h3>
        <div id="contentTarget"></div>
        
		<!--  
		<h3>Current Entity</h3>
        <sky-note app="notes" version="v1" path="notes/14:0/" ></sky-note>
        
   	    <h3>New Note</h3>
        <sky-load url="/notes/v1/notes/" selection="#showFormDiv" id="newNote" />
        -->
	  
	  </div>
	</div>
  </body>
</html>
>>