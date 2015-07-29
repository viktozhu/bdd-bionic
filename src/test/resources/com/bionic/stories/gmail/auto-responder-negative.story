Meta:
@issue BDD-2

Narrative:
As a google account user
I want to make sure that autoreply email is not sent in response to autoreply email
So that I can manage my emails properly and in short time

Scenario: Verify that autoreply email is not sent in response to autoreply email
Given an email was sent from first google account
When the second account sends autoreply email in response
Then the first account gets autoreply email
And doesn't send autoreply email in response