@singleEntityRefApplication 
Feature: account specific features 

    An account with a name...
    
    These tests assume that the user is authenticated and has the appropriate roles; initially,
    the database is empty (but there is no guarantee of the execution order of the tests).

Background: 
    Given a running AccountApplication 

Scenario: adding a simple account entity
    When I add an account with name 'theaccount'
    And I query all accounts 
    Then the result contains an account with name 'theaccount'

Scenario: adding a simple account without name yields error
    When I add an account without name
    Then I get a 'Bad Request' response
