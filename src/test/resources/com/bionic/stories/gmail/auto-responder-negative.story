Meta:
@ignore
@issue BDD-2


Narrative:
As a google account user
I want to make sure that autoreply email is not sent in response to autoreply email
So that I can manage my emails properly and in short time

Scenario: Verify that autoreply email is not sent in response to autoreply email
Given authorized connection to gmail as 'bionic.bdd' user
Given an email was sent by logged in user to 'bionic.bdd.test@gmail.com', with content '{
                                                                                           "messageSubject": "Hello message",
                                                                                           "header": "Hi, ",
                                                                                           "body": "This is my first email",
                                                                                           "footer": "Best regards! "
                                                                                         }'
When the second account sends autoreply email in response
Then the first account gets autoreply email
And doesn't send autoreply email in response