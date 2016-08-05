@singleEntityRefApplication 
Feature: account specific features 

    An account with a name 

Background: 
    Given a clean AccountApplication 

Scenario: querying the empty repository 
    When I query all accounts 
    Then the size of the result is 0.
    
Scenario: adding a simple account entity
    When I add an account with name 'theaccount'
    And I query all accounts 
    Then the size of the result is 1.

#Scenario: adding a simple account without name yields error
#    When I add an account without name
#    And I query all accounts 
#    Then the size of the result is 1.
