Feature: Belly
    
  Scenario: creating a simplistic application
    Given An Application with name 'notesApplication'
    When I query the applications name
    Then I'll get 'notesApplication'
