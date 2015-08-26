package com.bionic.jbehave.gmail;

import com.bionic.steps.GmailSteps;
import com.bionic.utils.PropertyLoader;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.gson.Gson;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GmailDefinitions {
    @Steps
    GmailSteps steps;

    @Given("authorized connection to gmail as '$user' user")
    public void authorizedConnection(String guser) {
        Gmail service = null;
        if (guser.equals("bionic.bdd"))
            service = steps.authorize(PropertyLoader.getProperty("project.bionic.bdd"), PropertyLoader.getProperty("secret.path.bionic.bdd"));
        if (guser.equals("bionic.bdd.test"))
            service = steps.authorize(PropertyLoader.getProperty("project.bionic.bdd.test"), PropertyLoader.getProperty("secret.path.bionic.bdd.test"));

        Assert.assertNotNull("Authorization failed", service);
        Serenity.getCurrentSession().put("service", service);
    }

    @Given("Auto-Responder application is running for '$user' user")
    @Pending
    public void runAutoResponder(String user) {

    }

    Message message = null;

    @When("an email was sent by logged in user to '$emailTo', with content '$content'")
    public void givenAnEmailWasSentFromFirstGoogleAccount(String emailTo, String content) {
        Gmail service = (Gmail) Serenity.getCurrentSession().get("service");
        message = steps.sendEmail(service, emailTo, content);
    }

    @When("the second account sends autoreply email in response")
    public void whenTheSecondAccountSendsAutoreplyEmailInResponse() {
        Gmail service = (Gmail) Serenity.getCurrentSession().get("service");
        steps.executeAutoResponder(service, message,"Sorry, I'm out of office");
    }

    @Then("'$user' user get autoreply email in response from '$emailFrom'")
    public void thenTheFirstAccountGetAutoreplyEmail(String user, String emailFrom) throws IOException {
        Gmail service = (Gmail) Serenity.getCurrentSession().get("service");
        String userID = user + "@gmail.com";
        assertTrue(steps.isAutoReplyReceived(service, userID, emailFrom));
    }

    @Then("'$user' user doesn't get autoreply email in response from '$emailFrom'")
    public void thenDoesntSendAutoreplyEmailInResponse(String user, String emailFrom) throws IOException {
        Gmail service = (Gmail) Serenity.getCurrentSession().get("service");
        String userID = user + "@gmail.com";
        assertFalse(steps.isAutoReplyReceived(service, userID, emailFrom));
    }

    @When("user I get list of emails")
    @Pending
    public void getEmailList() {
    }

    @Then("no new emails recevied")
    @Pending
    public void shouldNotBeNewEmails() {
    }

}
