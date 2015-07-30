package com.bionic.google;

import com.google.api.services.gmail.Gmail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;

/**
 * Created by Mostja on 29-Jul-15.
 */
public class GmailSender {
    private Gmail service;

    public GmailSender(Gmail service) {
        this.service = service;
    }

    public MimeMessage createEmail(String to, String from, String subject,
                                   String bodyText) {
        return null;
    }

    public MimeMessage createAutoReply(String to, String from) {
        return null;
    }

    public void sendMessage(MimeMessage email)
            throws MessagingException, IOException {
    }

}
