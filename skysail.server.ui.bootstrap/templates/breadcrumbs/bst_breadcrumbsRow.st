bst_breadcrumbsRow(model,user) ::= <<
  <div class="container"  style="height:30px;margin-top: 0px;padding: 0px 0px 0px 0px;z-index:100;position:relative;">
    <div class="row" style="background-color:#f5f5f5;height:30px;">
      <div class="col-md-10 col-xs-12">
       $breadcrumbs/bst_breadcrumb(model)$
	  </div>
	  <div class="col-md-2 sidebar hidden-xs">
      $if(user.developer)$
        $breadcrumbs/bst_developerLinks(model)$
      $else$
        <ul class="breadcrumb" style="text-align:right">
          <li>&nbsp;</li>
        </ul>
      $endif$
      </div>
    </div>
  </div>
>>