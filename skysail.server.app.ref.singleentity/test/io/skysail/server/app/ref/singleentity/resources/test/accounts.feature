@singleEntityRefApplication
Feature: [Ref.App SingleEntity] - account specific features - HTML 

    An account is an entity with a non-empty name, an iban and a non-empty creation date.
    
    These tests assume that the user is authenticated and has the appropriate roles; initially,
    the database is empty (but there is no guarantee of the execution order of the tests).
    
    The media type used here is not relevant as nothing gets actually rendered in these tests.

Background: 
    Given a running AccountApplication
    And I am logged in as 'admin'

@Creation
Scenario: adding a simple account entity
    When I add an account like this:
      | name | account_<random> |
    And I query all accounts
    Then the accounts list page contains such an account:
       | name      | account_<random> |

@Creation
Scenario: adding a simple account entity with iban
    When I add an account like this:
      | name | account_<random>       |
      | iban | DE00000000000000000000 |
    And I query all accounts
    Then the accounts list page contains such an account:
       | name | account_<random> |
       | iban | DE00000000000000000000 |
    
@Creation    
Scenario: getting "Created 201" after creating a new account
     When I add an account like this:
       | name | anotheraccount |
     Then I get a 'Created (201)' response

@Validation
Scenario: adding a simple account without name yields an error
    When I add an account like this:
       | name | |
    Then I get a 'Bad Request' response

@Security
Scenario: if the user sends the "created" property it should be ignored
     When I add an account like this:
       | name      | account_<random>        |
       | iban      | DE00000000000000000000  |
       | created   | 1472280000000           |
     And I open the account page
     Then the page contains:
       | name      | account_<random>        |
       | iban      | DE00000000000000000000  |
       | created   | <!>1472280000000        |

@Retrieval
Scenario: retrieving a created account again
     When I add an account like this:
       | name | a new one |
     And I open the account page
     Then the page contains 'a new one'

@Updating
Scenario: updating an account entity
     When I add an account like this:
       | name | another account |
     And I change its 'name' to 'another account II'
     And I open the account page
     Then the page contains 'another account II'

#https://github.com/orientechnologies/orientdb/issues/6306
@Deletion
Scenario: deleting an account
     When I add an account like this:
       | name | account2beDeleted |
     And I delete it again
     And I query all accounts
     Then the result does not contain an account with name 'account2beDeleted'

#JustMe
@Deletion
@Security
Scenario: deleting someone elses account yields error
     When I log in as 'otheruser'
     When I add an account like this:
       | name | account_<random> |
     And I log in as 'admin'
     And I try to delete it again
     Then I get a 'Forbidden' response

