@entities
Feature: application specific features
  
  An application defines a unit of related entities, value objects and repositories.

  Background: 
    Given An Application with name 'notesApplication'

 # Scenario: creating a simplistic application
 #   When I query the applications name
 #   Then I'll get 'notesApplication'

 # Scenario: creating an application with an entity
 #   When I add an entity called 'note'
 #   Then the list of entities of that application has size 1

 # Scenario: creating an application with two entities
 #   When I add an entity called 'note'
 #   And I add an entity called 'owner'
 #   Then the list of entities of that application has size 2

 # Scenario: adding same entity twice yields it was added only once
    
 #   Entities with same name (that is, id) are considered to have the same identity, meaning
 #   only one will be added to the application model.

#    When I add an entity called 'note'
 #   And I add an entity called 'note'
 #   Then the list of entities of that application has size 1

  #@EntitiesWithFields
  #Scenario: creating an application with an entity which has a field
   # When I add an entity called 'note'
    #And I add a field called 'title' of type 'java.lang.String' to that entity
    #Then the list of fields of that entity has size 1
