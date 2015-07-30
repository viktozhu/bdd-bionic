package com.bionic.jbehave.gmail;

import com.bionic.steps.GmailSteps;
import com.bionic.utils.PropertyLoader;
import com.google.api.services.gmail.Gmail;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by viktozhu on 7/23/15.
 */
public class GmailDefinitions {
    @Steps
    GmailSteps steps;

    @Given("authorized connection to gmail as '$user' user")
    public void authorizedConnection(String guser) {
        Gmail service = null;
        if (guser.equals("bionic.bdd"))
            service = steps.authorize(PropertyLoader.getProperty("secret.path.bionic.bdd"), PropertyLoader.getProperty("project.bionic.bdd"));

        Assert.assertNotNull("Authoprization failed", service);
        Serenity.getCurrentSession().put("service", service);
    }

    @When("user I get list of emails")
    public void getEmailList(){

    }

    @Then("no new emails recevied")
    public void shouldNotBeNewEmails(){

    }


    @Given("google account user")
    public void givenGoogleAccountUser() {
        // PENDING
    }

    @When("user receives a new email")
    public void whenUserReceivesANewEmail() {
        // PENDING
    }

    @When("autoResponder is executed")
    public void whenAutoResponderIsExecuted() {
        // PENDING
    }

    @Then("autoResponder sends auto-reply email for this email")
    public void thenAutoResponderSendsAutoreplyEmailForThisEmail() {
        // PENDING
    }

    @Given("an email was sent by logged in user to '$emailTo', with content '$content'")
    public void givenAnEmailWasSentFromFirstGoogleAccount(String emailTo, String content) {
        Gmail service = (Gmail) Serenity.getCurrentSession().get("service");
        steps.sendEmail(service,emailTo,content);
    }

    @When("the second account sends autoreply email in response")
    public void whenTheSecondAccountSendsAutoreplyEmailInResponse() {
        Gmail service = (Gmail) Serenity.getCurrentSession().get("service");
        steps.executeAutoResponder(service,"to");
    }

    @Then("the first account gets autoreply email")
    public void thenTheFirstAccountGetAutoreplyEmail() {

        Gmail service = (Gmail) Serenity.getCurrentSession().get("service");
        assertTrue(steps.isAutoReplyReceived(service,"from"));
    }

    @Then("doesn't send autoreply email in response")
    public void thenDoesntSendAutoreplyEmailInResponse() {
        Gmail service = (Gmail) Serenity.getCurrentSession().get("service");
        assertFalse(steps.isAutoReplyReceived(service,"from"));
    }


}
