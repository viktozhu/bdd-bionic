Meta:
@TIS-45
@ignore

Narrative:
In order to attract new users
System should email non registered users to sign-up with Teams in Space

Scenario: System email non registred user after his visit
Given non registered user visited system
When Team goes into Space
Then system sends sign-up email to this user

Examples:
|userName|userEmail|
|vasia|vasia@gmail.com|
|chuck norris|gmail@chuck norris|