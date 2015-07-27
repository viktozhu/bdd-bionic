package com.bionic.jbehave.gmail;

import com.bionic.google.GmailAuthorization;
import com.bionic.steps.GmailSteps;
import com.bionic.utils.PropertyLoader;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by viktozhu on 7/23/15.
 */
public class GmailDefinitions {
    @Steps
    GmailSteps steps;

    @Given("authorized connection to gmail")
    public void authorizedConnection() throws IOException {
        PropertyLoader.loadPropertys();
        Gmail service = GmailAuthorization.getGmailService();

        String user = "me";
        ListLabelsResponse listResponse =
                service.users().labels().list(user).execute();
        List<Label> labels = listResponse.getLabels();
        if (labels.size() == 0) {
            System.out.println("No labels found.");
        } else {
            System.out.println("Labels:");
            for (Label label : labels) {
                System.out.printf("- %s\n", label.getName());
            }
        }
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

    @Given("an email was sent from first google account")
    public void givenAnEmailWasSentFromFirstGoogleAccount() {
        // PENDING
    }

    @When("the second account sends autoreply email in response")
    public void whenTheSecondAccountSendsAutoreplyEmailInResponse() {
        // PENDING
    }

    @Then("the first account get autoreply email")
    public void thenTheFirstAccountGetAutoreplyEmail() {
        // PENDING
    }

    @Then("doesn't send autoreply email in response")
    public void thenDoesntSendAutoreplyEmailInResponse() {
        // PENDING
    }


}
