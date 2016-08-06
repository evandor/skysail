@singleEntityRefApplication 
Feature: account specific features 

    An account is an entity with a non-empty name, a iban and a non-empty creation date.
    
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

Scenario: retrieving an created account again
    When I add an account with name 'a new one'
    And I open the account page
    Then the page contains 'theaccount'

Scenario: updating an account entity
    When I add an account with name 'another account'
    And I change it's 'name' to 'another account II' 
    And I open the account page
    Then the page contains 'another account III'
