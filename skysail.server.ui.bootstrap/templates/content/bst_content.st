bst_content(user,converter,messages,model,request)::= <<
  <div class="row" style="background-color: #F8FAD4; width:100%;">
    <div class="col-md-12" style="width:100%;">
      $contentHeader(converter, messages, model)$
    </div>
  </div>
  <div class="row" style="width:800px">
    <div class="col-md-12" style="width:100%;border:10px solid red;">
    $if(model.form)$
      <div class="col-md-10" id="showFormDiv">
        <br>
        <sky-form 
        	context='$request.context$' 
        	target='$model.formTarget$' 
        	tabs='$model.tabsAsJson$' 
        	fields='$model.formfieldsWrapper.asJson$' 
        	data='$model.dataWrapper.asJson$'>
        </sky-form>
      </div>
      <div class="col-md-2" id="renderEntityInfoDiv">
                <br>
                $renderEntityInfo(model.formfields, messages, model)$
      </div>
    $elseif(model.relationTargetList)$
      <div class="col-md-12" id="showRelationTargetFormDiv">
                <br>
                $showRelationTargetForm(model.formfields, messages, model)$
      </div>
    $elseif(model.list)$
      <div id="renderTableDiv" style="width:100%;">
                <br>
				$if(converter.debug)$
                  Filter     [Param _f]: $model.filterParamValue$<br>
                  Sorting    [Param _s]: $model.sortingParamValue$<br>
                  Pagination [Param _p]: <br>
                  Query      [Param _q]: <br>
                  <br>
                $endif$
                <sky-table fields='$model.formfieldsWrapper.asJson$' request='$request.asJson$' data='$model.dataWrapper.asJson$'></sky-table>
      </div>
    $else$
              <div class="col-md-12" id="renderEntityDiv">
                <br>
                $renderEntity(user, model)$
              </div>
    $endif$
    </div>
  </div>
>>