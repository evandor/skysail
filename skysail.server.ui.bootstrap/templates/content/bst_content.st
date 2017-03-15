bst_content(user,converter,messages,model,request)::= <<
  <div class="container">
    <div class="row">
      <div class="col-md-16">
        <sky-content identifier="$model.resourceClassName$.message" content='' edit-base-url='$converter.editBaseUrl$' >
        $messages.("content.header").value$
        </sky-content>
      </div>
    </div>
    <div class="row">
    $if(model.form)$
      <div class="col-md-14" id="showFormDiv">
        <br>
		<form action="$model.formTarget$" method="POST" class="form-horizontal" role="form" id="form" name="form">
		  $model.formfieldsWrapper.formfields: { ff | $bst_formElement(model,converter,ff)$ };separator="\n"$ 
          <div class="form-group">
	        <div class="col-sm-offset-4 col-sm-12">
	          <button type="submit" name="submit" value="submit" class="btn btn-default" >submit</button>&nbsp;
	        </div>
  	      </div>      
    	</form>      
      </div>
      <div class="col-md-2" id="renderEntityInfoDiv">
        <br>
        $bst_renderEntityInfo(model.formfields, messages, model)$
      </div>
     

    $elseif(model.relationTargetList)$
 
      <div class="col-md-16" id="showRelationTargetFormDiv">
        <br>
        $showRelationTargetForm(model.formfields, messages, model)$
      </div>
 
    $elseif(model.list)$
 
      <div class="col-md-16" id="renderTableDiv">
        <br>
		$if(converter.debug)$
          Filter     [Param _f]: $model.filterParamValue$<br>
          Sorting    [Param _s]: $model.sortingParamValue$<br>
          Pagination [Param _p]: <br>
          Query      [Param _q]: <br>
          <br>
        $endif$
        <table class="table table-striped table-condensed table-hover ROWCLICK">
          <tr>
            $model.formfieldsWrapper.formfields: { ff | $bst_thElement(model,ff)$ };separator="\n"$ 
	  	    <th>Actions</th>
          </tr>
          $model.data: { row | $bst_trElement(model.formfieldsWrapper.formfields,row)$ };separator="\n"$ 
        </table>
        <div class="row">
    	    <div class="col-md-16">
        	  $bst_menuNav(model.createFormLinks)$
    	    </div>
        </div>
        
      </div>
 
    $else$
      <div class="col-md-16" id="renderEntityDiv">
        <br>
        $bst_renderEntity(user, model)$
      </div>
    $endif$
  </div>
  
</div>
>>