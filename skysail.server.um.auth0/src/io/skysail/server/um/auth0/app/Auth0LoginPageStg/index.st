index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="/Auth0UmApplication/v1/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/Auth0UmApplication/v1/css/jquery.growl.css"/>
    <script src="http://code.jquery.com/jquery.js"></script>
    <script src="http://cdn.auth0.com/js/lock-9.min.js"></script>
    <script src="/Auth0UmApplication/v1/js/jquery.growl.js" type="text/javascript"></script>
</head>
<body>
<div class="container">
    <script type="text/javascript">
        \$(function () {
            var error = false;
            if (error) {
                \$.growl.error({message: "Please log in"});
            } else {
                \$.growl({title: "Welcome!", message: "Please log in"});
            }
        });
        \$(function () {
            var lock = new Auth0Lock('bFXJcqk3OU7u1yKisnmrCw0TkEMl506d', 'skysail.eu.auth0.com');
            lock.showSignin({
                authParams: {
                    state: 'nonce=$first(model.rawData).state$',
                    // change scopes to whatever you like
                    // claims are added to JWT id_token - openid profile gives everything
                    scope: 'openid user_id name nickname email picture'
                },
                responseType: 'code',
                popup: false,
                callbackURL: 'http://localhost:2018'
            });
        });
    </script>
</div>
</body>
</html>


>>