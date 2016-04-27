@entities
Feature: entities specific features

  Entities behave like DDD entities
  
  @Equality
  Scenario: two entites with the same name [that is: id] are considered equal
    Given An Entity with name 'note'
	  And Another Entity with name 'note'
    Then the two entities are equal.

#  @Equality
#  Scenario: two entites with the same name [that is: id] are considered equal, even if they have fields that differ
#    Given An Entity with name 'note'
#    And I add a field called 'title' of type 'java.lang.String' to that entity
#	  And Another Entity with name 'note'
#    And I add a field called 'notatitle' of type 'java.lang.Integer' to that other entity
#    Then the two entities are equal.

  #Scenario: Creation of single entity
  #  Given An Entity with name 'note'
	#	Then the list of fields of that entity has size 0