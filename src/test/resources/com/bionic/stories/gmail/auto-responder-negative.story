Meta:
@ignore
@issue BDD-2


Narrative:
As a google account user
I want to make sure that autoreply email is not sent in response to autoreply email
So that I can manage my emails properly and in short time

Scenario: Verify that autoreply email is not sent in response to autoreply email
Given authorized connection to gmail as 'bionic.bdd.test' user
And Auto-Responder application is running for 'bionic.bdd' user
When an email was sent by logged in user to 'bionic.bdd@gmail.com', with content '{
                                                                                       "messageSubject": "Hello message",
                                                                                       "header": "Hi, ",
                                                                                       "body": "This is my first email",
                                                                                       "footer": "Best regards! "
                                                                                     }'
Then 'bionic.bdd.test' user get autoreply email in response from 'bionic.bdd@gmail.com'
When an email was sent by logged in user to 'bionic.bdd@gmail.com', with content '{
                                                                                           "messageSubject": "Auto-responce",
                                                                                           "header": "Hi, ",
                                                                                           "body": "This is my auto-respond email",
                                                                                           "footer": "Best regards! "
                                                                                         }'
Then 'bionic.bdd.test' user doesn't get autoreply email in response from 'bionic.bdd@gmail.com'




