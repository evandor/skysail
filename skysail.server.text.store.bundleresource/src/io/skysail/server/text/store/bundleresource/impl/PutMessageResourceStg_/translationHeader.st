translationHeader(model, renderer) ::= <<
          <table style="width:80%">
            <tr>
              <th>Translation Key</th>
              <td>$first(model.data).msgKey$</td>
              <td>The identifier of the message to be translated.</td>
            </tr>
            <tr>
              <th>Translation Store</th>
              <td>$first(model.data).store$</td>
              <td>This is where the translation is persisted.</td>
            </tr>
            <tr>
              <th>Translation Renderer</th>
              <td>$renderer$</td>
              <td>A translation can be applied "as-is", or post-rendered, e.g. as markdown</td>
            </tr>
            <tr>
              <th>Language</th>
              <td>Default</td>
              <td>Choose the language or create a new one...</td>
            </tr>
            <!--<tr>
              <th>redirecting to</th>
              <td>$model.redirectBackTo$</td>
              <td>where to go from here...</td>
            </tr>-->
          </table>

>>