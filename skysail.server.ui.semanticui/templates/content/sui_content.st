sui_content(user,converter,messages,model)::= <<

   <br><br>
  <div class="row">
    
    <div class="col-md-12" style="border-top: 1px dotted #a0a0a0;margin-top:-21px;">
      <div class="row" style="background-color: #F8FAD4">
        <div class="col-md-12" style="border-bottom: 1px dotted #a0a0a0;">
          $contentHeader(converter, messages, model)$
        </div>
      </div>
      <div class="row">
        <div class="col-md-12"  $if(converter.debug && user.principal)$style="border:3px solid yellow;"$endif$>
            $if(model.form)$
              <div class="col-md-10" id="showFormDiv">
                <br>
                $showForm(model.formfields, messages, model)$
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
              <div class="col-md-12" id="renderTableDiv">
                <br>
				$if(converter.debug)$
                  Filter     [Param _f]: $model.filterParamValue$<br>
                  Sorting    [Param _s]: $model.sortingParamValue$<br>
                  Pagination [Param _p]: <br>
                  Query      [Param _q]: <br>
                  <br>
                $endif$
                $table/sui_render(converter, messages, model)$
              </div>
            $else$
              <div class="col-md-12" id="renderEntityDiv">
                <br>
                $renderEntity(user, model)$
              </div>
            $endif$

        </div>
      </div>
      <div class="row" style="border-top: 1px dotted #a0a0a0; padding-top: 10px;">
        <div class="col-md-11">
            $menuNav(model.createFormLinks)$
        </div>
      </div>
    </div>
  </div>



>>