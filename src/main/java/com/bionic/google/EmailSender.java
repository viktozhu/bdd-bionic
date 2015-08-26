package com.bionic.google;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.*;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Multipart;

public class EmailSender {
    private static String userId;
    private static Gmail service;

    public EmailSender(Gmail service, String user) {
        this.service = service;
        this.userId = user;
    }

    public Message sendMessage(String emailTo, String json) throws MessagingException {
        return sendMimeMessage(formatMimeMessage(emailTo, json));
    }

    public Message sendMessage(String emailTo, String emailSubject, String emailBody) throws MessagingException {
        return sendMimeMessage(formatMimeMessage(emailTo, emailSubject, emailBody));
    }

    public Message sendMessage(String emailTo, File pathToJson) throws MessagingException, IOException {
        Reader reader = new FileReader(pathToJson);
        EmailContent emailContainerContent = new GsonBuilder().create().fromJson(reader, EmailContent.class);
        return sendMimeMessage(formatMimeMessage(emailTo, emailContainerContent));
    }

    public Message sendReplyTo(Message message, String replayMessage) throws MessagingException, IOException {
        Message fullMessage = new EmailGetter(service,userId).getMessage(message.getId());
        List<MessagePartHeader> headersList = fullMessage.getPayload().getHeaders();
        HashMap <String, String> headersMap = new HashMap<>();
        for (MessagePartHeader part: headersList) {
            headersMap.put(part.getName(), part.getValue());
        }
        String messageSubject = headersMap.get("Subject");
        String emailTo = headersMap.get("From");;
        String messageId = headersMap.get("Message-ID");
        String references = headersMap.get("References");
        references = references + " " + messageId;
        String threadId = fullMessage.getThreadId();
        return sendMimeMessage(threadId, formatMimeMessage(emailTo, messageSubject, replayMessage, messageId, references));
    }

    public Message createEmailWithAttachment(String to, String jsonString, String fileDir, String filename) throws MessagingException, IOException {
        MimeMessage email = formatMimeMessage(to, jsonString);
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        // mimeBodyPart.setContent(bodyText, "text/plain");
        mimeBodyPart.setHeader("Content-Type", "text/plain; charset=\"UTF-8\"");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        mimeBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(fileDir + filename);
        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName(filename);
        String contentType = Files.probeContentType(FileSystems.getDefault()
                .getPath(fileDir, filename));
        mimeBodyPart.setHeader("Content-Type", contentType + "; name=\"" + filename + "\"");
        mimeBodyPart.setHeader("Content-Transfer-Encoding", "base64");
        multipart.addBodyPart(mimeBodyPart);
        email.setContent(multipart);
        return sendMimeMessage(email);
    }

    private MimeMessage formatMimeMessage(String emailTo, String json) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(userId));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(emailTo));
        EmailContent emailContainerContent = new GsonBuilder().create().fromJson(json, EmailContent.class);
        String bodyText = emailContainerContent.getHeader() + ",\n"
                + emailContainerContent.getBody() + "\n"
                + emailContainerContent.getFooter();
        email.setSubject(emailContainerContent.getMessageSubject());
        email.setText(bodyText);
        return email;
    }

    private MimeMessage formatMimeMessage(String emailTo, EmailContent emailContainerContent) throws MessagingException {
        String bodyText = emailContainerContent.getHeader() + ",\n"
                + emailContainerContent.getBody() + "\n"
                + emailContainerContent.getFooter();
        return formatMimeMessage(emailTo, emailContainerContent.getMessageSubject(), bodyText);
    }

    private MimeMessage formatMimeMessage(String emailTo, String messageSubject, String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(userId));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(emailTo));
        email.setSubject(messageSubject);
        email.setText(bodyText);
        return email;
    }

    private MimeMessage formatMimeMessage(String emailTo, String messageSubject, String bodyText, String replyTo, String references) throws MessagingException {
        MimeMessage email = formatMimeMessage(emailTo, messageSubject, bodyText);
        email.setHeader("In-Reply-To", replyTo);
        email.setHeader("References", references);
        return email;
    }

    private Message sendMimeMessage(MimeMessage email) throws MessagingException {
        Message message = convertMimeMessageToMessage(email);
        try {
            message = service.users().messages().send(userId, message).execute();
        } catch (IOException e) {
            throw new MessagingException("Cannot send message", e);
        }
        return message;
    }

    private Message sendMimeMessage(String threadId, MimeMessage email) throws MessagingException {
        Message message = convertMimeMessageToMessage(email);
        message.setThreadId(threadId);
        try {
            message = service.users().messages().send(userId, message).execute();
        } catch (IOException e) {
            throw new MessagingException("Cannot send message", e);
        }
        return message;
    }

    private Message convertMimeMessageToMessage(MimeMessage email) throws MessagingException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            email.writeTo(bytes);
        } catch (IOException e) {
            throw new MessagingException("Cannot write to stream", e);
        }
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}