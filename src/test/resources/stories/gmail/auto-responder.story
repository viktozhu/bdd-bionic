Meta:
@ignore
@issue BDD-3


Narrative:
FOR google account users
WHO want to keep their colleagues informed about inability of reading their emails
THE Auto-Responder product is a Mail AutoResponder App
THAT sends an automated reply to anyone who emails you

Scenario: Positive Scenario for Auto-Responder App
Given authorized connection to gmail as 'bionic.bdd.test' user
And Auto-Responder application is running for 'bionic.bdd' user
When an email was sent by logged in user to 'bionic.bdd@gmail.com', with content '{
                                                                                               "messageSubject": "Hello message",
                                                                                               "header": "Hi, ",
                                                                                               "body": "This is my first email",
                                                                                               "footer": "Best regards! "
                                                                                             }'
Then 'bionic.bdd.test' user get autoreply email in response from 'bionic.bdd@gmail.com'
