package com.thisisabir.chatapp.model;

import java.util.Date;

public class ChatMessage{

    private String messgaeText;
    private String messageUser;
    private long messageTime;

    // creating constructor without time coz time will be change based on chat
    public ChatMessage(String messgaeText, String messageUser) {
        this.messgaeText = messgaeText;
        this.messageUser = messageUser;

        // get time formula
        messageTime = new Date().getTime();
    }

    // need an empty constructor for firebase
    public ChatMessage() {
    }

    // getter setter method for chat Message
    public String getMessgaeText() {
        return messgaeText;
    }

    public void setMessgaeText(String messgaeText) {
        this.messgaeText = messgaeText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
