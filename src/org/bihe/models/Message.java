package org.bihe.models;

import com.google.gson.Gson;
import org.bihe.sevices.MessageForSending;

import java.time.ZonedDateTime;

public class Message {
    private Integer ID;
    private String message;
    private User senderUser;
    private User receiverUser;
    private ZonedDateTime created_date; // It is a better practice, but MariaDB does not support timezones literals.
    private String messageType;
    public Message() {
    }

    public Message(String message, User senderUser, User receiverID, ZonedDateTime created_date, String messageType) {
        this.message = message;
        this.senderUser = senderUser;
        this.receiverUser = receiverID;
        this.created_date = created_date;
        this.messageType = messageType;
    }

    public String toJsonSendingFormat() {
        Gson gson = new Gson();
        MessageForSending messageForSending = new MessageForSending(this);
        return gson.toJson(messageForSending);
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }


    public User getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(User receiverUser) {
        this.receiverUser = receiverUser;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(User senderUser) {
        this.senderUser = senderUser;
    }

    public ZonedDateTime getCreated_date() {
        return created_date;
    }

    public void setCreated_date(ZonedDateTime created_date) {
        this.created_date = created_date;
    }
}
