#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
@tag
Feature: entities features

  Entities behave like DDD entities
  
  Background:
    Given An Application with name 'notesApplication'
  
  @EntitiesWithFields
  Scenario: creating an application with an entity which has a field
    When I add an entity called 'note'
    And I add a field called 'title' of type 'java.lang.String' to that entity
    Then the list of fields of that entity has size 1
    

  @Equality
  Scenario: Title of your scenario
    Given An Application with name 'notesApplication'
    When I add an entity called 'note' 
    And I add a field called 'title' of type 'java.lang.String' to that entity
    Then I validate the outcomes

