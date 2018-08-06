Feature: Cucumber New Solution Login
#This is to check test result for Pass test case

@task1 @door
Scenario: Verify Login to system
Given Verify login page open
When Login controls are avaliable 
Then Login sucessfully

#This is to check test result for Failed test case
@task2 @door
Scenario: Sign-in to facility
Given Verify login page open
When Login controls are avaliable
Then Sign to facility sucessfully

