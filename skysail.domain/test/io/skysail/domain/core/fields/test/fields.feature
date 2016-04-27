@Fields
Feature: fields specific features
  
  Fields represent entities attributes information like name, type, order and so on.

  @FieldCreation
  Scenario: Checking the basic properties after calling fieldModels constructor
    Given A Field with name 'title' of type 'java.lang.String'
    Then the fields id is 'title'
    And the fields name is 'title'
    And the fields type is 'public final class java.lang.String'

  @FieldRepresentation
  Scenario: Checking the toString Representation of a new FieldModel
    Given A Field with name 'count' of type 'java.lang.Integer'
    Then the string representation of that field is 'FieldModel(id=title, type=Integer, inputType=null)'
