package com.bionic.jbehave.gmail;

import com.bionic.steps.GmailSteps;
import com.bionic.utils.PropertyLoader;
import com.google.api.services.gmail.Gmail;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by viktozhu on 7/23/15.
 */
public class GmailDefinitions {
    @Steps
    GmailSteps steps;

    private static final String account1 = "bionic.bdd@gmail.com";
    private static final String account2 = "bionic.bdd.test@gmail.com";

    @Given("authorized connection to gmail as '$user' user")
    public void authorizedConnection(String guser) {
        Gmail service = null;
        if (guser.equals("bionic.bdd"))
            service = steps.authorize(PropertyLoader.getProperty("project.bionic.bdd"),PropertyLoader.getProperty("secret.path.bionic.bdd"));
        if (guser.equals("bionic.bdd.test"))
            service = steps.authorize(PropertyLoader.getProperty("project.bionic.bdd.test"),PropertyLoader.getProperty("secret.path.bionic.bdd.test"));

        Assert.assertNotNull("Authorization failed", service);
        Serenity.getCurrentSession().put("service", service);
    }

    @When("user I get list of emails")
    @Pending
    public void getEmailList(){

    }

    @Then("no new emails recevied")
    @Pending
    public void shouldNotBeNewEmails(){

    }

    @When("user receives a new email")
    @Pending
    public void whenUserReceivesANewEmail(String to, String content) {
        Gmail service = (Gmail) Serenity.getCurrentSession().get("service");
        //steps.sendEmail(service, to, content);
    }

    @When("Test user sends email to '$user' user")
    public void whenTestUserSendsEmailTobionicbddUser(String user) {

        File jsonEmail = new File("/");
        Gson json = new Gson();
        json.toJson("src/main/resources/auto-reply.json");
        steps.sendEmail((Gmail) Serenity.getCurrentSession().get("service"),account1,json);
    }

    @Then("Test user receives autoreply")
    @Pending
    public void thenTestUesrReceivesAutoreply() {
        // PENDING
    }

    @Then("doesn't send autoreply email in response")
    public void thenDoesntSendAutoreplyEmailInResponse() {
        steps.executeAutoResponderOn(account1);
        assertFalse(steps.shouldReceiveAutoReply(account2, account1));
    }

    @Given("an email was sent by logged in user to '$emailTo', with content '$content'")
    @Pending
    public void givenAnEmailWasSentFromFirstGoogleAccount(String emailTo, String content) {
        Gmail service = (Gmail) Serenity.getCurrentSession().get("service");
        //steps.sendEmail(service, emailTo, content);
    }

    @When("the second account sends autoreply email in response")
    public void whenTheSecondAccountSendsAutoreplyEmailInResponse() {
        Gmail service = (Gmail) Serenity.getCurrentSession().get("service");
        steps.executeAutoResponder(service, "to");
    }

    @Then("the first account gets autoreply email")
    public void thenTheFirstAccountGetAutoreplyEmail() {

        Gmail service = (Gmail) Serenity.getCurrentSession().get("service");
        assertTrue(steps.isAutoReplyReceived(service, "from"));
    }

}
