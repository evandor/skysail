bst_breadcrumbsRow(model,converter,user) ::= <<
  <div class="container" style="z-index:100;position:relative;margin-top:0px;">
    <div class="row" style="background-color:#e1e1e1;">
      <div class="col-md-12 col-xs-12">
       $breadcrumbs/bst_breadcrumb(model)$
	  </div>
	  <div class="col-md-4 sidebar hidden-xs">
      $if(user.developer)$
        $breadcrumbs/bst_developerLinks(model,converter)$
      $else$
        <ul class="breadcrumb" style="text-align:right">
          <li>&nbsp;</li>
        </ul>
      $endif$
      </div>
    </div>
  </div>
>>