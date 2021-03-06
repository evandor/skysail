mdb_head() ::= <<
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta http-equiv="x-ua-compatible" content="ie=edge">

    <title>skysail</title>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.0/css/font-awesome.min.css">

    <!-- Bootstrap core CSS -->
    <link href="/_ui/mdb/4.2.0/css/bootstrap.min.css" rel="stylesheet">

    <!-- Material Design Bootstrap -->
    <link href="/_ui/mdb/4.2.0/css/mdb.min.css" rel="stylesheet">
    
    <!-- polymer -->
    <script src="/_polymer/bower_components/webcomponentsjs/webcomponents-lite.min.js"></script>
    
   	<link rel="import" href="/_polymer/bower_components/iron-ajax/iron-ajax.html">
	<link rel="import" href="/_polymer/bower_components/test-el/test-el.html">
	<link rel="import" href="/_polymer/bower_components/juicy-html/juicy-html.html">
	<link rel="import" href="/_polymer/bower_components/sky-menu/sky-menu.html">
	<link rel="import" href="/_polymer/bower_components/sky-load/sky-load.html">
	<link rel="import" href="/_polymer/bower_components/sky-echo-html/sky-echo-html.html">

    <link rel="import" href="/_ui/mdb/sky-mdb/sky-table.html">
    <link rel="import" href="/_ui/mdb/sky-mdb/sky-entity.html">

    <!-- Template styles -->
    <style rel="stylesheet">
        /* TEMPLATE STYLES */
        
        main {
            padding-top: 3rem;
            padding-bottom: 2rem;
        }
        
        .extra-margins {
            margin-top: 1rem;
            margin-bottom: 2.5rem;
        }
    </style>
    
    <script src="/_jslib/keycloak/2.5.0/keycloak.js"></script>
    <script>
        var keycloak = Keycloak('/_jslib/keycloak/2.5.0/keycloak.json');
        keycloak.init({ onLoad: 'login-required' }).success(function(authenticated) {
            alert(authenticated ? 'authenticated' : 'not authenticated');
        }).error(function() {
            alert('failed to initialize');
        });
    </script>
>>