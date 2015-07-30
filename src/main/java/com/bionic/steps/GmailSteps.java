package com.bionic.steps;

import com.bionic.google.EmailSender;
import com.bionic.google.GmailAuthorization;
import com.bionic.google.GmailReceiver;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.gson.Gson;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import org.junit.Assert;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Created by viktozhu on 7/27/15.
 */
public class GmailSteps extends ScenarioSteps {

    @Step
    public Gmail authorize(String applicationName, String pathToClientSecret) {
        try {
            GmailAuthorization gmailAuthorization = null;
            gmailAuthorization = new GmailAuthorization(applicationName, pathToClientSecret);

            Gmail service = gmailAuthorization.getGmailService();
            return service;

        } catch (GeneralSecurityException e) {
            Assert.fail("Incorrect SECRET KEY:" +e.toString());
        } catch (IOException e) {
            Assert.fail("Incorrect SECRET KEY:" +e.toString());
        }

        return null;
    }

    @Step
    public void sendEmail(Gmail service, String mailTo, String content) {
        EmailSender sender = new EmailSender(service);
        Gson json = new Gson();
        json.toJson(content);
        try {
            sender.sendMessage(mailTo, json);
        } catch (MessagingException e) {
            Assert.fail("Message sending error: " + e.toString());
        } catch (IOException e) {
            Assert.fail("Message sending error: " + e.toString());
        }
    }

    public void executeAutoResponder(Gmail service, String to){
        EmailSender sender = new EmailSender(service);
        sender.sendAutoReplyMessage(to);
    }

    public boolean isAutoReplyReceived(Gmail service, String from) {
        GmailReceiver receiver = new GmailReceiver(service);
        List<Message> receivedMessages = receiver.getUnreadMessages();
        return receivedMessages.stream().anyMatch(m -> isAutoReply(m));
    }

    private boolean isAutoReply(Message message){
        // TODO: implement check logic
        return true;
    }
}
