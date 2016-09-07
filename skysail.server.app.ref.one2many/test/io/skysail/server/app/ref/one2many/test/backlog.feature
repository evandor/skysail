@backlog 
Feature: [Ref.App One2Many] - todolists specific features 

    A TodoList is an entity with a non-empty name.
    
    These tests assume that the user is authenticated and has the appropriate roles; initially,
    the database is empty (but there is no guarantee of the execution order of the tests).

Background: 
    Given a running OneToManyApplication
    
Scenario: updating a todolist entity
     When I add a todolist like this:
       | listname | another todolist |
     And I change its 'name' to 'another todolist II'
     And I open the todolist page
     Then the page contains 'another todolist II'

    