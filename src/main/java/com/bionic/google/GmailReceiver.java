package com.bionic.google;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import java.util.List;

/**
 * Created by Mostja on 30-Jul-15.
 */
public class GmailReceiver {
    private Gmail service;

    public GmailReceiver(Gmail service) {
        this.service = service;
    }

    public List<Message> getUnreadMessages() {
        return null;
    }
}
