package com.bionic.jbehave.gmail;

import com.bionic.google.GmailAuthorization;
import com.bionic.steps.GmailSteps;
import com.bionic.utils.PropertyLoader;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Created by viktozhu on 7/23/15.
 */
public class GmailDefinitions {
    @Steps
    GmailSteps steps;

    @BeforeStories
    public void init() {
        PropertyLoader.loadPropertys();
    }

    @Given("authorized connection to gmail")
    public void authorizedConnection() throws IOException {
        PropertyLoader.loadPropertys();
        GmailAuthorization gmailAuthorization = null;
        try {
            gmailAuthorization = new GmailAuthorization("bdd-project2", "src/main/resources/secrets/bionic.bdd.test.secret.json");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        Gmail service = gmailAuthorization.getGmailService();

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
        steps.authorizeInit();
    }

    @When("user receives a new email")
    public void whenUserReceivesANewEmail() {
        steps.userSendsEmail("bionic.bdd@gmail.com", "bionic.bdd.test@gmail.com", "message4Test", "Some text...");
    }

    @When("Auto-Responder is executed")
    @Alias("the second account sends autoreply email in response")
    public void whenAutoResponderIsExecuted() {
        steps.executeAutoResponderOn("bionic.bdd.test@gmail.com");
    }

    @Then("Auto-Responder sends auto-reply for this email")
    @Alias("the first account get autoreply email")
    public void thenAutoResponderSendsAutoreplyEmailForThisEmail() {
        steps.shouldReceiveAutoReply("bionic.bdd@gmail.com", "bionic.bdd.test@gmail.com");
    }

    @Given("an email was sent from first google account")
    public void givenAnEmailWasSentFromFirstGoogleAccount() {
        steps.userSendsEmail("bionic.bdd@gmail.com", "bionic.bdd.test@gmail.com", "message4Test", "Some text...");
    }

    @Then("doesn't send autoreply email in response")
    public void thenDoesntSendAutoreplyEmailInResponse() {
        steps.executeAutoResponderOn("bionic.bdd@gmail.com");

    }


}
