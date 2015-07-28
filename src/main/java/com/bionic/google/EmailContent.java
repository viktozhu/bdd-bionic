package com.bionic.google;

import com.google.gson.annotations.SerializedName;

public class EmailContent {
    @SerializedName("messageSubject")
    private String messageSubject;
    @SerializedName("header")
    private String header;
    @SerializedName("body")
    private String body;
    @SerializedName("footer")
    private String footer;

    public EmailContent(String messageSubject, String header, String body, String footer) {
        this.messageSubject = messageSubject;
        this.header = header;
        this.body = body;
        this.footer = footer;
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public void setMessageSubject(String messageSubject) {
        this.messageSubject = messageSubject;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }
}