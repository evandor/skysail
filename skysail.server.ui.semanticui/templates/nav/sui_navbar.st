sui_navbar(user, messages, converter, model) ::= <<
    <div class="ui fixed menu">
    
      <a href="#" class="header item">
        <img class="logo" src="assets/images/logo.png">
        skysail
      </a>
      <a href="#" class="item">Home</a>
      <div class="ui simple dropdown item">
        Styling <i class="dropdown icon"></i>
        <div class="menu">
          <a class="item" href='?_styling='>none</a>
          $nav/sui_renderTemplateProvider(model.styling)$
        </div>
      </div>
      
      $nav/sui_dropdown("Styling2",model.styling2)$
      
    </div>
>>
