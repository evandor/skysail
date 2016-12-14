bst_main(user, converter, messages, model) ::= <<
    <div class="row main-row fill">
      <div class="col-sm-2 sidebar hidden-xs">
        <h3>Navigation</h3>
        <p>Content</p>
        <p>Content</p>
        <p>Content</p>
        <p><sky-dropdown data='[{"label":"default_1","selected":true,"link":"thelink"},{"label":"default_1","selected":true,"link":"thelink"}]'></sky-dropdown></p>
      </div>
      <div class="col-sm-10 col-xs-12 main-content-area">
		$content/bst_content(user,converter,messages,model)$      
      </div>
    </div>
>>
