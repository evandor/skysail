@entities
Feature: entities specific features

  Entities behave like DDD entities
  
  @Equality
  Scenario: two entites with the same name [that is: id] are considered equal
    Given 1An Entity with name 'note'
	  And 1Another Entity with name 'note'
    Then 1the two entities are equal.

