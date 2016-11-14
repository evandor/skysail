@plineBackend 
Feature: [Pline Backend] - registration specific features 

    A Registration is an entity with a name and an email address.
    
    These tests assume that the user is authenticated and has the appropriate roles; initially,
    the database is empty (but there is no guarantee of the execution order of the tests).

Background: 
    Given a running PlineApplication
    
@JustMe
Scenario: registration of a new user
    When I add a registration like this:
      | name  | theName_<random>  |
      | email | mail@somewhere.go |
    And I query all registrations
    Then the registrations page contains such a registration:
      | name  | theName_<random>  |
      | email | mail@somewhere.go |

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
    