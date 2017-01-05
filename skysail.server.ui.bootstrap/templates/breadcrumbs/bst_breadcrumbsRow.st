bst_breadcrumbsRow(model,user) ::= <<
  <div class="row" style="background-color:#f5f5f5; height:40px; position:relative;top:51px;z-index:200;border-bottom: 1px dotted gray">
    <div class="col-md-10">
        $breadcrumbs/bst_breadcrumb(model)$
    </div>
    <div class="col-md-2">
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