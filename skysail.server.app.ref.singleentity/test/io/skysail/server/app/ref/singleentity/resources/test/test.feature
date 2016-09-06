@localtests
Feature: [Ref.App SingleEntity] - account specific features - HTML 


Background: 
    Given a running AccountApplication

#https://github.com/orientechnologies/orientdb/issues/6306
Scenario: deleting an account
     When I add an account like this:
       | name | account2beDeleted |
     And I delete it again
     And I query all accounts
     Then the result does not contain an account with name 'account2beDeleted'
