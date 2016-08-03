@entities 
Feature: account specific features 

	An account

#Background: 
#	
Scenario: creating a simplistic application 
	Given a clean AccountApplication
	When I query all accounts 
	Then the size of the result is 0.