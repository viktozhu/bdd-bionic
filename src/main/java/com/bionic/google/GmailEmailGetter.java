package com.bionic.google;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.ListThreadsResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.Thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GmailEmailGetter {
    private static final String USER_ID = "me";
    private static final String UNREAD_EMAIL_QUERY = "is: unread";
    private static final String INBOX_EMAIL_QUERY = "in: inbox";
    private Gmail email;

    public GmailEmailGetter(Gmail email) {
        this.email = email;
    }

    /**
     * Get Message with given ID.
     *
     * @param service   Authorized Gmail API instance.
     * @param userId    User's email address. The special value "me"
     *                  can be used to indicate the authenticated user.
     * @param messageId ID of Message to retrieve.
     * @return Message Retrieved Message.
     * @throws IOException
     */
    private Message getMessage(Gmail service, String userId, String messageId)
            throws IOException {
        Message message = service.users().messages().get(userId, messageId).execute();

        //Getting a snippet just for the testing purpose
        System.out.println("Message snippet: " + message.getSnippet());

        return message;
    }

    /**
     * List all Messages of the user's mailbox matching the query.
     *
     * @param service Authorized Gmail API instance.
     * @param userId  User's email address. The special value "me"
     *                can be used to indicate the authenticated user.
     * @param query   String used to filter the Messages listed.
     * @throws IOException
     */
    public List<Message> listMessagesMatchingQuery(Gmail service, String userId,
                                                   String query) throws IOException {
        ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();

        List<Message> messages = new ArrayList<>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(userId).setQ(query)
                        .setPageToken(pageToken).execute();
            } else {
                break;
            }
        }

        for (Message message : messages) {
            System.out.println(message.toPrettyString());
        }

        return messages;
    }

    /**
     * List all Threads of the user's mailbox matching the query.
     *
     * @param service Authorized Gmail API instance.
     * @param userId  User's email address. The special value "me"
     *                can be used to indicate the authenticated user.
     * @param query   String used to filter the Threads listed.
     * @throws IOException
     */
    public List<Thread> listThreadsMatchingQuery(Gmail service, String userId,
                                                 String query) throws IOException {
        ListThreadsResponse response = service.users().threads().list(userId).setQ(query).execute();
        List<Thread> threads = new ArrayList<>();
        while (response.getThreads() != null) {
            threads.addAll(response.getThreads());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().threads().list(userId).setQ(query).setPageToken(pageToken).execute();
            } else {
                break;
            }
        }

        for (Thread thread : threads) {
            System.out.println(thread.toPrettyString());
        }

        return threads;
    }

    public List<Message> getUnreadMessages() {
        String unreadInInboxEmailQuery = UNREAD_EMAIL_QUERY + " + " + INBOX_EMAIL_QUERY;
        try {
            return listMessagesMatchingQuery(email, USER_ID, unreadInInboxEmailQuery);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}