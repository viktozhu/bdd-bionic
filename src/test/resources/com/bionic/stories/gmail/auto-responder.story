Meta:
@issue BDD-2


Narrative:
FOR google account users
WHO want to keep their colleagues informed about inability of reading their emails
THE Auto-Responder product is a Mail AutoResponder App
THAT sends an automated reply to anyone who emails you

Scenario: Positive Scenario for Auto-Responder App
Given authorized connection to gmail as 'bionic.bdd' user
And Auto-Responder application is running
When user receives a new email
Then Auto-Responder sends auto-reply for this email
