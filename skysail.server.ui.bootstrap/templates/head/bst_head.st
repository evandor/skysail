bst_head() ::= <<
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>$messages.("productName").translated$</title>
    <link rel="shortcut icon" href="/webapp/favicon.ico" type="image/x-icon" >
    
    <!-- Bootstrap -->
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
      <![endif]-->
    
    <link href="https://fonts.googleapis.com/css?family=Inconsolata|Roboto|Slabo+27px" rel="stylesheet">
    
   	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>

    <!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	
   	<!-- polymer -->
    <script src="/_polymer/bower_components/webcomponentsjs/webcomponents-lite.min.js"></script>
    
   	<link rel="import" href="/_polymer/bower_components/iron-ajax/iron-ajax.html">
	<link rel="import" href="/_polymer/bower_components/test-el/test-el.html">
	<link rel="import" href="/_polymer/bower_components/juicy-html/juicy-html.html">
	<link rel="import" href="/_polymer/bower_components/sky-menu/sky-menu.html">
	<link rel="import" href="/_polymer/bower_components/sky-load/sky-load.html">
	<link rel="import" href="/_polymer/bower_components/sky-echo-html/sky-echo-html.html">

	<link rel="import" href="/_ui/bootstrap/sky-bst/sky-entity.html">
	<link rel="import" href="/_ui/bootstrap/sky-bst/sky-form.html">
    <link rel="import" href="/_ui/bootstrap/sky-bst/sky-menuitem.html">
    <link rel="import" href="/_ui/bootstrap/sky-bst/sky-dropdown.html">
    <link rel="import" href="/_ui/bootstrap/sky-bst/sky-table.html">
    
    <!-- http://stackoverflow.com/questions/19089384/twitter-bootstrap-3-two-columns-full-height -->
    <style>  
		html,body,.container {
		    height:100%;
		}
		.container {
		    display:table;
		    width: 100%;
		    margin-top: -50px;
		    padding: 50px 0 0 0; /*set left/right padding according to needs*/
		    box-sizing: border-box;
		}
		
		.row {
		    height: 100%;
		    display: table-row;
		}
		
		.row .no-float {
		  display: table-cell;
		  float: none;
		}
		.fill {
		  width:100%;
		  height:100%;
		  min-height:100%;
		  padding:0px;
		}

    </style>

>>