@entities
Feature: [DesignerApplication] - entity specific features 

    Todo: Entities...
    
    These tests assume that the user (called "admin") is authenticated and has the appropriate roles; 
    initially, the database is empty (but there is no guarantee of the execution order of the tests).
    
    The media type used here is not relevant as nothing gets actually rendered in these tests.

Background: 
    Given a running DesignerApplication
	And an application like this:
      | name | testapp_<random> |

Scenario: adding a simple entity to the application
    When I add an entity like this:
      | name | Entity_<random> |
    And I query all applications
    Then the applications list page contains an application with an entity like:
       | name      | Entity_<random> |
 
#@JustMe
Scenario: adding a simple entity with lowercase name yields error
    When I add an entity like this:
      | name | entity_<random> |
    Then I get a 'Bad Request (400)' response
 
Scenario: adding a simple entity with a name containing spaces yields error
    When I add an entity like this:
      | name | entity blank _<random> |
    Then I get a 'Bad Request (400)' response
 