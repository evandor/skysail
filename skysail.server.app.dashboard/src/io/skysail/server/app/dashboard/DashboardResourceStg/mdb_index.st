mdb_index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
    $head/mdb_head()$
  </head>
  
  <body>
  
    <header>
	  $nav/mdb_navbar(user, messages, converter, model)$
    </header>
    
    <div id="content">
    <main>
        <!--Main layout-->
        <div class="container-fluid">
            <!--First row-->
            <div class="row" id="StartTour" style="margin-left:15px;margin-right:15px">
                <div class="col-md-12">
                    <div class="row" style="background: url('img/kvb1.jpg') no-repeat center center;
    background-size: 100% auto;
    min-height: 0.2vh;border-color:#00254D;border-style:solid;border-width: 0px 0px 10px 0px;height:20.0vh;">

                        <div class="col-md-6 offset-md-3">
                        <form class="form-inline" id="searchForm" method="POST" action="http://localhost:9000/mp-search/results">
                            <div class="row" style="margin-top:5.0vh;height:3.0rem">
                                <div class="col-md-11 supertop" style="padding:0px;width:auto">
                                    <input class="form-control z-depth-4" id="searchInput" name="searchInput" placeholder="Hier können Sie Ihren Suchbegriff eingeben..." style="background: rgba(255,255,255,0.9);width:40vw;padding-left:10px;margin:0px;border-color:#bbbbbb;border-style:solid;border-width: 1px 1px 1px 1px;" type="text" />
                                </div>
                                <div class="col-md-1 flex-center" onclick="\$('#searchForm').submit();" style="background:#bbbbbb; cursor:pointer">
                                    <i aria-hidden="true" class="fa fa-search" style="color:white;font-size:24px"></i>
                                </div>
                            </div>

                        </form>
                            <script>
                                var options = {
                                    url: function (phrase) {
                                        return 'http://localhost:9000/mp-search/' + "/hits?searchInput=" + phrase;
                                    },
                                    getValue: "formularname",

                                    requestDelay: 400,
                                    highlightPhrase: true,
                                    adjustWidth: false,
                                    list: {
                                        maxNumberOfElements: 10,
                                        match: {enabled:false}
                                    },
                                    template: {
                                        type: "custom",
                                        method: function (value, item) {
                                            var icon = "file-o";
                                            if (item.format == "PDF") icon ="fa-file-pdf-o";
                                            var html_icon = "<i class='fa "+icon+"' aria-hidden='true'></i> ";
                                            var html_path = "<br/><small><i style='color:#909090;'>Formulare > "+item.kategorie+"</small></i>";
                                            var html_output = "<a href='" + item.url + "' target='_blank'>" + html_icon + value + html_path +"</a>";
                                            return html_output;
                                        },
                                        fields: {
                                            link: "url"
                                        }
                                    }
                                };
                                \$("#searchInput").easyAutocomplete(options);
                            </script>
                         </div>

                    </div>
                </div>
            </div>
            <!--/.Widget-Grid-->
            <div class="row" style="margin-left:30px;margin-right:30px;margin-left:0px;margin-right:0px">
                <div class="gridster" style="display:block">
                    <ul>
                        <li data-col="1" data-row="1" data-sizex="1" data-sizey="1">
                            <div class="row z-depth-1 hoverable animated bounceInDown grey lighten-5" id="widget_upload">

                                <a class="col-sm-2 shadow-box kvred flex-center" href="https://dienste.kvb.kv-safenet.de/ondes/app/start.htm">
                                    <i class="fa fa-upload fa-2x fa-inverse"></i>
                                </a>

                                <a class="col-sm-9" href="https://dienste.kvb.kv-safenet.de/ondes/app/start.htm" style="margin-top:0.2vh">
                                    <p class="card-head">Dateien sicher einreichen</p>
                                    <span class="card-text">Hier können Sie Ihre Abrechnungs- und Behandlungsdateien hochladen</span>
                                </a>
                                <div class="pull-right widget-icons" style="float:right;padding-top:2px;padding-right: 5px">
                                    <i aria-hidden="true" class="fa fa-arrows"></i>
                                    <a data-content="Hilfetext zu Unterlagen für die KV einreichen" data-placement="left" data-toggle="popover" data-trigger="focus" tabindex="0" title="Einreichen"><i aria-hidden="true" class="fa fa-question-circle"></i></a>
                                </div>
                            </div>
                        </li>
                        <li data-col="2" data-row="1" data-sizex="1" data-sizey="1">
                            <div class="row z-depth-1 hoverable animated bounceInDown grey lighten-5">
                                <a class="col-sm-2 shadow-box kvblue flex-center" href="https://dienste.kvb.kv-safenet.de/eule-web/app/akteSuche?execution=e1s1">
                                    <i class="fa fa-folder-open fa-2x fa-inverse"></i>
                                </a>

                                <a class="col-sm-9" href="https://dienste.kvb.kv-safenet.de/eule-web/app/akteSuche?execution=e1s1" style="margin-top:0.2vh">
                                    <p class="card-head">Dokumentenarchiv</p>
                                    <span class="card-text">Alle KV-Dokumente auf einen Blick und Klick</span>
                                </a>
                                <div class="pull-right widget-icons" style="float:right;padding-top:2px;padding-right: 5px">
                                    <i aria-hidden="true" class="fa fa-arrows"></i>
                                    <a data-content="Hilfetext zu Dokumentenarchiv" data-placement="left" data-toggle="popover" data-trigger="focus" tabindex="0" title="Dokumentenarchiv"><i aria-hidden="true" class="fa fa-question-circle"></i></a>
                                </div>
                            </div>
                        </li>
                        <li data-col="3" data-row="1" data-sizex="1" data-sizey="1">
                            <div class="row z-depth-1 hoverable animated bounceInDown grey lighten-5">
                                <a class="col-sm-2 shadow-box kvblue flex-center" href="https://dienste.kvb.kv-safenet.de/opal-web/">
                                    <i class="fa fa-list-alt fa-2x fa-inverse"></i>
                                </a>

                                <a class="col-sm-9" href="https://dienste.kvb.kv-safenet.de/opal-web/" style="margin-top:0.2vh">
                                    <p class="card-head">Dokumentationen</p>
                                    <span class="card-text">Dokumentieren von Behandlungsleistungen</span>

                                </a>
                                <div class="pull-right widget-icons" style="float:right;padding-top:2px;padding-right: 5px">
                                    <i aria-hidden="true" class="fa fa-arrows"></i>
                                    <a data-content="Hilfetext zu online Dokumentationen" data-placement="left" data-toggle="popover" data-trigger="focus" tabindex="0" title="Dokumentationen"><i aria-hidden="true" class="fa fa-question-circle"></i></a>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>


            <script>
                \$(document).ready(function () {
                    // Popovers Initialization
                    \$(function () {
                        \$('[data-toggle="popover"]').popover()
                    })
                 /*   \$("#widget_upload").mpload('http://localhost:9000/mp-upload/widget', '#widget', function (response, status, xhr) {
                        if (status == "error") {
                            var msg = "Sorry but there was an error: ";
                            \$("#widget_upload").html(msg + xhr.status + " " + xhr.statusText);
                        }
                    }); */
                });
            </script>
        </div>
        <!--/.Main layout-->

    </main>

</div>


<footer class="footer pull-right">
    <ul class="list-inline" style="margin-right:30px">
        <li class="list-inline-item"><a href="information.html#top">Kontakt</a></li>
        <li class="list-inline-item"><a href="information.html#Impressum">Impressum</a></li>
        <li class="list-inline-item"><a href="information.html#Datenschutz">Datenschutz</a></li>
    </ul>
</footer>
    
    
    
  </body>
</html>
>>