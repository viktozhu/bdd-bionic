package com.bionic.steps;

import com.bionic.google.EmailGetter;
import com.bionic.google.EmailSender;
import com.bionic.google.GmailAuthorization;
import com.google.api.services.gmail.Gmail;
import com.google.gson.Gson;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import org.junit.Assert;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GmailSteps extends ScenarioSteps {


    @Step
    public Gmail authorize(String applicationName, String pathToClientSecret) {
        try {
            GmailAuthorization gmailAuthorization = null;
            gmailAuthorization = new GmailAuthorization(applicationName, pathToClientSecret);

            Gmail service = gmailAuthorization.getGmailService();
            return service;

        } catch (GeneralSecurityException | IOException e) {
            Assert.fail("Incorrect SECRET KEY:" + e.toString());
        }

        return null;
    }

    @Step
    public void sendEmail(Gmail service, String mailTo, Gson json) {
        EmailSender sender = new EmailSender(service);
        /*try {
            sender.sendMessage(mailTo, json);
        } catch (MessagingException e) {
            Assert.fail("Message sending error: " + e.toString());
        } catch (IOException e) {
            Assert.fail("Message sending error: " + e.toString());
        }*/
    }

    @Step
    public void executeAutoResponder(Gmail service, String to) {
        EmailSender sender = new EmailSender(service);
        // sender.sendAutoReplyMessage(to);
    }

    @Step
    public boolean isAutoReplyReceived(Gmail service, String userID, String from) throws IOException {
        EmailGetter getter = new EmailGetter(service);
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String query = "from:" + from + " subject:I am currently out of the office. is:unread";
        return (getter.listMessagesMatchingQuery(service, userID, query)).size() > 0;
    }


}
