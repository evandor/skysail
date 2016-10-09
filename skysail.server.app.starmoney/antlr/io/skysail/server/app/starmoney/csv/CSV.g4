grammar CSV;

// from https://github.com/bkiers/antlr4-csv-demo/blob/master/src/grammar/CSV.g4

/*@header {
  package csv;
}*/

file returns [List<List<String>> data]  
@init {$data = new ArrayList<List<String>>();}  
 : (row {$data.add($row.list);})+ EOF  
 ; 
  
row returns [List<String> list]  
@init {$list = new ArrayList<String>();}  
 : a=value {$list.add($a.val);} (SemiColon b=value {$list.add($b.val);})* (LineBreak | EOF)  
 ;

value returns [String val]  
 : SimpleValue {$val = $SimpleValue.text;}  
 | QuotedValue   
   { 
     $val = $QuotedValue.text; 
     $val = $val.substring(1, $val.length()-1); // remove leading- and trailing quotes 
     $val = $val.replace("\"\"", "\""); // replace all `""` with `"` 
   }  
 ;  

SemiColon  
 : ';'  
 ;  
  
LineBreak  
 : '\r'? '\n'  
 | '\r'  
 ;  
  
SimpleValue  
 : ~[;\r\n"]+  
 ;  
  
QuotedValue  
 : '"' ('""' | ~'"')* '"'  
 ; 