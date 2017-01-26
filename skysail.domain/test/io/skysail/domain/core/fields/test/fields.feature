@Fields
Feature: fields specific features
  
  FieldModels represent entities attributes information like name, type, order and so on.

  @FieldModelCreation
  Scenario: Checking the basic properties after calling fieldModels constructor
    Given A Field with name 'title' of type 'java.lang.String'
    Then the fields 'id' attribute value is 'test|title' 
    And the fields 'type' attribute value is 'class java.lang.String'

  @FieldModelCreation
  Scenario: The inputType is null if not set explicitly
    Given A Field with name 'title' of type 'java.lang.String'
    Then the fields 'inputType' attribute value is ''

  @FieldModelCreation
  Scenario: The inputType is set to the provided value
    Given A Field with name 'title' of type 'java.lang.String'
    And the inputType is set to 'PASSWORD'
    Then the fields 'inputType' attribute value is 'PASSWORD'

  @FieldModelEquality
  Scenario: two fields are considered equal if they have the same id
    Given A Field with name 'theTitle' of type 'java.lang.String', referred to as 'A'
    # Then the title of field 'A' is 'theTitle'
  

  @FieldModelRepresentation
  Scenario: Checking the toString Representation of a new FieldModel
    Given A Field with name 'count' of type 'java.lang.Integer'
    Then the string representation of that field is 'FieldModel(id=test|title, type=Integer, inputType=null)'

  @BuildBehavior
  Scenario: a pending implementation
    
    This is meant to create a "yellow", pending build step in jenkins.
    
    Given A Field with name 'count' of type 'java.lang.Integer'
	  Then something happens which has not been implemented
	    