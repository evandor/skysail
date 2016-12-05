index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
    
<!--	<link rel="import" href="/_polymer/bower_components/emoji-rain/emoji-rain.html">
	<link rel="import" href="/_polymer/bower_components/app-layout/app-layout.html">
	
	<script src="/_polymer/bower_components/webcomponentsjs/webcomponents-lite.min.js"></script>

	<link rel="import" href="/_polymer/bower_components/app-layout/app-drawer/app-drawer.html">
	<link rel="import" href="/_polymer/bower_components/app-layout/app-header/app-header.html">
	<link rel="import" href="/_polymer/bower_components/app-layout/app-toolbar/app-toolbar.html">
	<link rel="import" href="/_polymer/bower_components/app-layout/demo/sample-content.html">
	<link rel="import" href="/_polymer/bower_components/iron-icons/iron-icons.html">
	<link rel="import" href="/_polymer/bower_components/paper-icon-button/paper-icon-button.html">
	<link rel="import" href="/_polymer/bower_components/paper-progress/paper-progress.html">-->
  </head>
  
  <body>
	<!--<base href="https://user-content-dot-custom-elements.appspot.com/PolymerElements/app-layout/v0.10.6/app-layout/">-->
	<base href="/_polymer/bower_components/app-layout/">
	
	<script src="../webcomponentsjs/webcomponents-lite.min.js"></script>

	<link rel="import" href="app-drawer/app-drawer.html">
	<link rel="import" href="app-header/app-header.html">
	<link rel="import" href="app-toolbar/app-toolbar.html">
	<link rel="import" href="demo/sample-content.html">
	<link rel="import" href="../iron-icons/iron-icons.html">
	<link rel="import" href="../paper-icon-button/paper-icon-button.html">
	<link rel="import" href="../paper-progress/paper-progress.html">
	
	
	<link rel="import" href="/_polymer/bower_components/iron-ajax/iron-ajax.html">
	
	<style is="custom-style">
	  body {
	    margin: 0;
	    font-family: 'Roboto', 'Noto', sans-serif;
	    -webkit-font-smoothing: antialiased;
	    background: #f1f1f1;
	  }
	  app-toolbar {
	    background-color: #4285f4;
	    color: #fff;
	  }
	  paper-icon-button + [main-title] {
	    margin-left: 24px;
	  }
	  paper-progress {
	    display: block;
	    width: 100%;
	    --paper-progress-active-color: rgba(255, 255, 255, 0.5);
	    --paper-progress-container-color: transparent;
	  }
	  app-header {
	    @apply(--layout-fixed-top);
	    color: #fff;
	    --app-header-background-rear-layer: {
	      background-color: #ef6c00;
	    };
	  }
	  app-drawer {
	    --app-drawer-scrim-background: rgba(0, 0, 100, 0.8);
	    --app-drawer-content-container: {
	      background-color: #B0BEC5;
	    }
	  }
	  sample-content {
	    padding-top: 64px;
	  }
	</style>
	<app-header reveals>
	  <app-toolbar>
	    <paper-icon-button icon="menu" onclick="drawer.toggle()"></paper-icon-button>
	    <div main-title>notes</div>
	    <!--<paper-icon-button icon="delete"></paper-icon-button>-->
	    <paper-icon-button icon="search"></paper-icon-button>
	    <!--<paper-icon-button icon="close"></paper-icon-button>-->
	    <!--<paper-progress value="10" indeterminate bottom-item></paper-progress>-->
	  </app-toolbar>
	</app-header>
	<app-drawer id="drawer" swipe-open></app-drawer>
	<sample-content size="2"></sample-content>
	
	<iron-ajax
    auto
    url="https://www.googleapis.com/youtube/v3/search"
    params='{"part":"snippet", "q":"polymer", "key": "YOUTUBE_API_KEY", "type": "video"}'
    handle-as="json"
    on-response="handleResponse"
    debounce-duration="300"></iron-ajax>
	
	
  </body>
</html>
>>