Meta:
@issue BDD-2

Narrative:
As a google account user
I want AutoResponder replies on my new emails
So that I can keep my collegeas informed about my absence at work

Scenario: Positive Scenario for AutoResponder App
Given google account user
When user receives a new email
And autoResponder is executed
Then autoResponder sends auto-reply email for this email
