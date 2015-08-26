package com.bionic.google;

import com.bionic.utils.PropertyLoader;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.List;

public class GmailApp {
    private static final int THREE_MINUTES_IN_MILLISECONDS = 3*60*1000;
    private static GoogleAuthorization googleAuthorization;
    private static Gmail gmail;
    private static String user;

    public static void main(String[] args) throws MessagingException, IOException, InterruptedException, GeneralSecurityException {
        PropertyLoader.loadProperties();

        // Unfortunately to be able to work with dofferent user there should be secrets for them,
        // so use only predefined
        user = PropertyLoader.getProperty("email.bionic.bdd");
        googleAuthorization = new GoogleAuthorization(PropertyLoader.getProperty("project.bionic.bdd"), PropertyLoader.getProperty("secret.path.bionic.bdd"));
        gmail = googleAuthorization.getGmailService();

        EmailGetter emailGetter = new EmailGetter(gmail,user);
        Long internalDateOfLast;
        Message lastMessage = emailGetter.lastMessage();
        if( lastMessage == null) {
            internalDateOfLast = 0L;
        } else {
            internalDateOfLast = emailGetter.getMessage(lastMessage.getId()).getInternalDate();
        }

        HashSet<String> handledThreadId = new HashSet<>();
        long tStart = System.currentTimeMillis();
        long tEnd = tStart + THREE_MINUTES_IN_MILLISECONDS;
        while (System.currentTimeMillis() < tEnd) {
            Thread.sleep(1000);
            System.out.println(System.currentTimeMillis());
            EmailSender emailSender = new EmailSender(gmail, user);
            List<Message> unreadMessages = emailGetter.getUnreadMessages();
            for (Message message : unreadMessages) {
                Message fullMessage = emailGetter.getMessage(message.getId());
                if (internalDateOfLast < fullMessage.getInternalDate() && !handledThreadId.contains(message.getThreadId())) {
                    Message replayMessage = emailSender.sendReplyTo(fullMessage, "Sorry, but I am currently out of the office.");
                    handledThreadId.add(replayMessage.getThreadId());
                }
            }
        }
    }
}