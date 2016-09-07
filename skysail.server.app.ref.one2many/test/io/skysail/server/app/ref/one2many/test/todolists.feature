@one2manyRefApplication 
Feature: [Ref.App One2Many] - todolists specific features 

    A TodoList is an entity with a non-empty name.
    
    These tests assume that the user is authenticated and has the appropriate roles; initially,
    the database is empty (but there is no guarantee of the execution order of the tests).

Background: 
    Given a running OneToManyApplication
    
Scenario: adding a simple todolist entity
    When I add a todolist like this:
      | listname | todolist_<random> |
    And I query all todolists
    Then the todolists page contains such a todolist:
       | listname | todolist_<random> |

Scenario: getting "Created 201" after creating a new account
     When I add a todolist like this:
       | listname | anotheraccount |
     Then I get a 'Created (201)' response
    
Scenario: adding a todolist without listname yields an error
    When I add a todolist like this:
       | listname | |
    Then I get a 'Bad Request' response

Scenario: retrieving a created account again
    When I add a todolist like this:
       | listname | a new one |
    And I open the todolist page
    Then the todolist page contains 'a new one'
    
Scenario: updating a todolist entity
     When I add a todolist like this:
       | listname | another todolist |
     And I change its 'name' to 'another todolist II'
     And I open the todolist page
     Then the page contains 'another todolist II'
    