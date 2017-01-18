bst_index(user, messages, converter, model,request) ::= <<
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>jQuery contextMenu</title>

    <link rel="stylesheet" href="https://swisnl.github.io/jQuery-contextMenu/css/screen.css" type="text/css"/>
    <link rel="stylesheet" href="https://swisnl.github.io/jQuery-contextMenu/css/theme.css" type="text/css"/>
    <link rel="stylesheet" href="https://swisnl.github.io/jQuery-contextMenu/css/theme-fixes.css" type="text/css"/>
    <link href="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.contextMenu.js" type="text/javascript"></script>
</head>
<body>




<p><span class="context-menu-one btn btn-neutral">right click me</span></p>

<script type="text/javascript">
    \$(function() {
        \$.contextMenu({
            selector: '.context-menu-one', 
            callback: function(key, options) {
                var m = "clicked: " + key;
                window.console && console.log(m) || alert(m); 
            },
            items: {
                "edit": {name: "Edit", icon: "edit"},
                "cut": {name: "Cut", icon: "cut"},
               copy: {name: "Copy", icon: "copy"},
                "paste": {name: "Paste", icon: "paste"},
                "delete": {name: "Delete", icon: "delete"},
                "sep1": "---------",
                "quit": {name: "Quit", icon: function(){
                    return 'context-menu-icon context-menu-icon-quit';
                }}
            }
        });

        \$('.context-menu-one').on('click', function(e){
            console.log('clicked', this);
        })    
    });
</script>


</body>
</html>

>>