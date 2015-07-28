package com.bionic.google;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class EmailSender {
    private String pathToJsonWithAutoReply = "src\\main\\resources\\auto-reply.json";
    private String userMailbox;
    private Gmail service;

    public EmailSender(Gmail service, String userMailbox) {
        this.service = service;
        this.userMailbox = userMailbox;
    }

    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to Email address of the receiver.
     * @param subject Subject of the email.
     * @param bodyText Body text of the email.
     * @return MimeMessage to be used to send email.
     * @throws MessagingException
     */
    public MimeMessage createEmail(String to, String subject,
                                          String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(userMailbox));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to Email address of the receiver.
     * @param subject Subject of the email.
     * @param bodyText Body text of the email.
     * @param fileDir Path to the directory containing attachment.
     * @param filename Name of file to be attached.
     * @return MimeMessage to be used to send email.
     * @throws MessagingException
     */
    public MimeMessage createEmailWithAttachment(String to, String subject,
                                                        String bodyText, String fileDir, String filename) throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        InternetAddress tAddress = new InternetAddress(to);
        InternetAddress fAddress = new InternetAddress(userMailbox);

        email.setFrom(fAddress);
        email.addRecipient(javax.mail.Message.RecipientType.TO, tAddress);
        email.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(bodyText, "text/plain");
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

        return email;
    }

    /**
     * Create a MimeMessage of the auto-reply.
     *
     * @param to Email address of the receiver.
     */
    private MimeMessage createAutoReplyEmail(String to) throws MessagingException, IOException {
        Reader reader = null;
        try {
            reader = new FileReader(pathToJsonWithAutoReply);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        EmailContent emailContainerContent = new GsonBuilder().create().fromJson(reader, EmailContent.class);
        String bodyText = emailContainerContent.getHeader() + ",\n"
                + emailContainerContent.getBody() + "\n"
                + emailContainerContent.getFooter();
        MimeMessage email = createEmail(to, emailContainerContent.getMessageSubject(), bodyText);
        return email;
    }

    /**
     * Create a Message from an email
     *
     * @param email Email to be set to raw of message
     * @return Message containing base64url encoded email.
     * @throws IOException
     * @throws MessagingException
     */
    public Message createMessageWithEmail(MimeMessage email)
            throws MessagingException, IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        email.writeTo(bytes);
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    /**
     * Send an email from the user's mailbox to its recipient.
     *
     * @param email Email to be sent.
     * @throws MessagingException
     * @throws IOException
     */
    public void sendMessage(MimeMessage email)
            throws MessagingException, IOException {
        Message message = createMessageWithEmail(email);
        message = service.users().messages().send(userMailbox, message).execute();
       // System.out.println("Message id: " + message.getId());
       //System.out.println(message.toPrettyString());
    }

    /**
     * Send an email with auto-reply from the user's mailbox to its recipient.
     *
     * @param to Email address of the receiver.
     */
    public void sendAutoReplyMessage(String to) {
        Message message = null;
        try {
            message = createMessageWithEmail(createAutoReplyEmail(to));
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            message = service.users().messages().send(userMailbox, message).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}