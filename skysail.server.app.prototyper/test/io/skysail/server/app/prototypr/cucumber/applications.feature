@applications
Feature: [DesignerApplication] - application specific features 

    An Application is a container for entities with a name. Applications define a runnable unit,
    i.e. they are used for code generation. The DesignerApplication is used to define (create and
    maintain) and run applications (generate code and execute it).
    
    These tests assume that the user (called "admin") is authenticated and has the appropriate roles; 
    initially, the database is empty (but there is no guarantee of the execution order of the tests).
    
    The media type used here is not relevant as nothing gets actually rendered in these tests.

Background: 
    Given a running DesignerApplication

Scenario: adding a simple application entity
    When I add an application like this:
      | name | application_<random> |
    And I query all applications
    Then the applications list page contains such an application:
       | name      | application_<random> |
       | owner     | admin                |

Scenario: adding a simple application entity with description and project name
    When I add an application like this:
      | name        | application_<random> |
      | description | a dummy app          |
      | projectName | the project          |
    And I query all applications
    Then the applications list page contains such an application:
      | name        | application_<random> |
      | description | a dummy app          |
      | projectName | the project          |
      | owner       | admin                |

Scenario: getting "Created 201" after creating a new application
    When I add an application like this:
      | name | application_<random> |
    Then I get a 'Created (201)' response

Scenario: adding a simple application without name yields an error
    When I add an application like this:
       | name | |
    Then I get a 'Bad Request' response

Scenario: adding a simple application with a name with blanks yields an error
    When I add an application like this:
       | name | a new app |
    Then I get a 'Bad Request' response

#@Ignore
#@JustMe
Scenario: if the user sends the "owner" property it should be ignored
    When I add an application like this:
       | name    | application_<random> |
       | owner   | attacker             |
    And I open the application page
    Then the page contains:
       | name    | application_<random> |
       | owner   | admin                |
       
Scenario: retrieving a created application again
    When I add an application like this:
       | name    | application_<random> |
    And I open the application page
    Then the page contains:
       | name    | application_<random> |

Scenario: updating an application entity
    When I add an application like this:
       | name    | application_<random> |
     And I change its 'name' to 'another account II'
    And I open the application page
    Then the page contains:
       | name    | application_<random> |


