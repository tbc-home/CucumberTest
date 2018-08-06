Feature: annotation 

   
Scenario: Verify Login to system
Given Verify login page
When I enter username as "TOM"
And I enter password as "JERRY"

And Relogin option should be available 
Then Login should fail 


#Scenario with BUT 

Scenario: Sign-in to facility
Given Verify login page
When I enter username as "TOM"
But Relogin option should be available 
#And I enter password as "JERRY"
Then Login should fail  
