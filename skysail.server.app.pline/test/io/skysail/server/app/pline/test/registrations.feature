@plineBackend 
Feature: [Pline Backend] - registration specific features 

    A Registration is an entity with a name and an email address.
    
    These tests assume that the user is authenticated and has the appropriate roles; initially,
    the database is empty (but there is no guarantee of the execution order of the tests).

Background: 
    Given a running PlineApplication
    
#@JustMe
Scenario: registration of a new user
    When I add a registration like this:
      | name  | theName_<random>  |
      | email | mail@somewhere.go |
    And I query all registrations
    Then the registrations page contains such a registration:
      | name  | theName_<random>  |
      | email | mail@somewhere.go |

Scenario: getting "Created 201" after creating a new registration
     When I add a registration like this:
      | name  | theName_<random>  |
      | email | mail@somewhere.go |
     Then I get a 'Created (201)' response
    
Scenario: adding a registration without name yields an error
    When I add a todolist like this:
      | name  | |
      | email | mail@somewhere.go |
    Then I get a 'Bad Request' response

Scenario: retrieving a created registration again
    When I add a registration like this:
      | name  | a new one |
      | email | mail@somewhere.go |
    And I open the registration page
    Then the registration page contains 'a new one'
    
Scenario: updating a registration entity
     When I add a registration like this:
      | name  | theName_<random>  |
      | email | mail@somewhere.go |
     And I change its 'name' to 'another registration'
     And I open the registration page
     Then the page contains 'another registration'
    