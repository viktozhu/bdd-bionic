package com.bionic.steps;

import com.bionic.google.GmailAuthorization;
import com.bionic.google.GmailReceiver;
import com.bionic.google.GmailSender;
import com.bionic.utils.PropertyLoader;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import net.thucydides.core.steps.ScenarioSteps;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Created by viktozhu on 7/27/15.
 */
public class GmailSteps extends ScenarioSteps {
    final String applicationName = "bdd-project";
    final String pathToClientSecret = "src/main/resources/secrets/bionic.bdd.secret.json";

    public void sendEmail(String from, String to) throws IOException, MessagingException, GeneralSecurityException {
        Gmail service = createGmailService();
        GmailSender sender = new GmailSender(service);
        String subject = "Test Message";
        String bodyText = "Hello, world!";
        MimeMessage message = sender.createEmail(to, from, subject, bodyText);
        sender.sendMessage(message);
    }

    public void executeAutoResponder(String from, String to) throws IOException, GeneralSecurityException, MessagingException {
        Gmail service = createGmailService();
        GmailSender sender = new GmailSender(service);
        MimeMessage message = sender.createAutoReply(to, from);
        sender.sendMessage(message);
    }

    public boolean isAutoReplyReceived(String from) throws IOException, GeneralSecurityException {
        Gmail service = createGmailService();
        GmailReceiver receiver = new GmailReceiver(service);
        List<Message> receivedMessages = receiver.getUnreadMessages();
        return receivedMessages.stream().anyMatch(m -> isAutoReply(m));
    }

    private Gmail createGmailService() throws IOException, GeneralSecurityException {
        PropertyLoader.loadPropertys();
        GmailAuthorization gmailAuthorization = null;
        gmailAuthorization = new GmailAuthorization(applicationName, pathToClientSecret);
        return gmailAuthorization.getGmailService();
    }

    private boolean isAutoReply(Message message){
        // TODO: implement check logic
        return true;
    }
}
