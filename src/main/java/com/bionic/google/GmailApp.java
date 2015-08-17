package com.bionic.google;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.List;

public class GmailApp {
    public static final int THREE_MINUTES_IN_MILLISECONDS = 3*60*1000;

    public static void main(String[] args) throws MessagingException, IOException, InterruptedException {
        GmailAuthorization gmailAuthorization = null;
        Gmail gmail = null;
        try {
            gmailAuthorization = new GmailAuthorization("bdd-project", "src/main/resources/secrets/bionic.bdd.secret.json");
            gmail = gmailAuthorization.getGmailService();
        } catch (IOException | GeneralSecurityException e) {
            System.out.println("Error during Authorization " + e.toString());
            System.exit(1);
        }

        //Get internalDate of last message
        EmailGetter emailGetter = new EmailGetter(gmail);
        Long internalDateOfLast;
        Message lastMessage = emailGetter.lastMessage();
        if( lastMessage == null) {
            internalDateOfLast = 0L;
        } else {
            internalDateOfLast = emailGetter.getMessage(gmail,"bionic.bdd@gmail.com", lastMessage.getId()).getInternalDate();
        }

        HashSet<String> handledThreadId = new HashSet<>();
        EmailSender emailSender = new EmailSender(gmail);
        long tStart = System.currentTimeMillis();
        long tEnd = tStart + THREE_MINUTES_IN_MILLISECONDS;
        while (System.currentTimeMillis() < tEnd) {
            Thread.sleep(3000);
            System.out.println(".");
            List<Message> unreadMessages = emailGetter.getUnreadMessages();
            for (Message message : unreadMessages) {
                Message fullMessage = emailGetter.getMessage(gmail,"bionic.bdd@gmail.com", message.getId());
                if (internalDateOfLast < fullMessage.getInternalDate() && !handledThreadId.contains(message.getThreadId())) {
                    Message replayMessage = emailSender.sendReplyTo(fullMessage, "Sorry, but I am currently out of the office.");
                    System.out.println("Auto-reply has been sent");
                    handledThreadId.add(replayMessage.getThreadId());
                }
            }
        }
    }
}