package com.bionic.google;

import com.bionic.google.EmailSender;
import com.bionic.google.GmailAuthorization;
import com.bionic.google.EmailGetter;
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
//        GmailAuthorization gmailAuthorization = null;
//        try {
//            gmailAuthorization = new GmailAuthorization("bdd-project", "src/main/resources/secrets/gmail/bionic.bdd.json");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        }
//        Gmail gmail = null;
//        try {
//            gmail = gmailAuthorization.getGmailService();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        EmailGetter emailGetter = new EmailGetter(gmail);
//        Long internalDateOfLast;
//        Message lastMessage = emailGetter.lastMessage();
//        if( lastMessage == null) {
//            internalDateOfLast = 0L;
//        } else {
//            internalDateOfLast = emailGetter.getMessage(lastMessage.getId()).getInternalDate();
//        }
//
//        HashSet<String> handledThreadId = new HashSet<>();
        long tStart = System.currentTimeMillis();
        long tEnd = tStart + THREE_MINUTES_IN_MILLISECONDS;
        while (System.currentTimeMillis() < tEnd) {
            Thread.sleep(1000);
            System.out.println(System.currentTimeMillis());
//            EmailSender emailSender = new EmailSender(gmail);
//            List<Message> unreadMessages = emailGetter.getUnreadMessages();
//            for (Message message : unreadMessages) {
//                Message fullMessage = emailGetter.getMessage(message.getId());
//                if (internalDateOfLast < fullMessage.getInternalDate() && !handledThreadId.contains(message.getThreadId())) {
//                    Message replayMessage = emailSender.sendReplyTo(fullMessage, "auto-reply");
//                    handledThreadId.add(replayMessage.getThreadId());
//                }
//            }
        }
    }
}