Meta:
@ignore
@issue BDD-003

Narrative:
As a user of gDriveConsole
I want 'verify' functionality works properly with two different files
So that I can be sure in integrity of data

Scenario: Verifies that '-verify' option works properly with two different files
Given a file test.txt with size of 3 Mb
And a differ file test.pptx with size of 3 Mb is in the same directory
When I run application with parameters '-verify test.txt test.pptx'
Then App notifies me that files are different


Scenario: Verifies that '-verify' option works properly when one file dosn't exist
Given a file test.txt with size of 3 Mb
And another file test2.pptx isn't in the same directory
When I run application with parameters '-verify test.txt test2.pptx'
Then App notifies me that files are different