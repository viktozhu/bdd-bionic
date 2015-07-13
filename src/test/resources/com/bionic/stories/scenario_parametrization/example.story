Meta:
@run @not

Narrative:
As a user
I want to perform an action
So that I can achieve a business goal

Scenario: scenario description
Given user logged in as 'admin'
When I update story description, set name=<storyName>, description=<storyDescription>
Then story board contains:
|story|description|
|someStory|for admin user|
|another story|but the same run|

Examples:
|storyName|storyDescription|
|firstRun|other parameters stays the same|
|secondRun|yet, not changes|