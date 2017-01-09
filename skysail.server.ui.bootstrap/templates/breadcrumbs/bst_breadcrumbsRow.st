bst_breadcrumbsRow(model,user) ::= <<
  <div class="row" style="background-color:#f5f5f5; height:55px; position:relative;top:59px;z-index:200; border:1px solid blue">

	<div class="col-sm-2 sidebar hidden-xs">
	 <h6>Starmoney</h6>
    </div>
    <div class="col-sm-8 col-xs-12">
        $breadcrumbs/bst_breadcrumb(model)$
	</div>
	<div class="col-sm-2 sidebar hidden-xs">
      $if(user.developer)$
        $developerLinks(model)$
      $else$
        <ul class="breadcrumb" style="text-align:right">
          <li>&nbsp;</li>
        </ul>
      $endif$
    </div>

  </div>
>>