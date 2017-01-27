bst_developerLinks(model,converter) ::= <<
  <ul class="breadcrumb" style="text-align:right;margin-bottom:0px;padding:5px 20px;">
    $if(converter.edit)$
      <li><a href='javascript:document.cookie="mode=default;path=/";window.location.reload(true)'><i class="fa fa-edit" aria-hidden="true" style="color:green" title="toggle Editmode"></i></a></li>
    $else$
      <li><a href='javascript:document.cookie="mode=edit;path=/";window.location.reload(true)'><i class="fa fa-edit" aria-hidden="true" style="color:#bfbfbf" title="toggle Editmode"></i></a></li>
    $endif$
    <li class="context-menu-export"><i class="fa fa-arrow-circle-o-right" aria-hidden="true"></i> Export</li>
  </ul>
>>

