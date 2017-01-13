bst_content(user,converter,messages,model,request)::= <<
<div class="container">
  <!--<div class="row" style="height:30px;">
    <div class="col-md-12">
      $contentHeader(converter, messages, model)$
    </div>
  </div>-->
  <div class="row">
    
      <div class="col-md-10" id="showFormDiv">
        <br>
		<form action="$model.formTarget$" method="POST" class="form-horizontal" role="form" id="form" name="form">
	
		  $model.formfieldsWrapper.formfields: { ff | $content/bst_formElement(ff)$ };separator="\n"$ 

          <div class="form-group">
	        <div class="col-sm-offset-2 col-sm-10">
	          <button type="submit" name="submit" value="submit" class="btn btn-default" >submit</button>&nbsp;
	        </div>
  	      </div>      
    	</form>      
    	
    	+++
    	<sky-google-maps-places-autocomplete apiKey="$first(model.data).googleApiKey$"></sky-google-maps-places-autocomplete>
    	
    	
    	
      </div>
      <div class="col-md-2" id="renderEntityInfoDiv">
                <br>
                $renderEntityInfo(model.formfields, messages, model)$
      </div>
    
  </div>
  <div class="row">
  	<div class="col-md-12">
    	$menuNav(model.createFormLinks)$
  	</div>
  </div>
</div>
>>