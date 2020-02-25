package org.bihe.models;

import java.time.ZonedDateTime;

public class Message {
    private Integer ID;
    private String message;
    private Integer senderID;
    private Integer receiverID;
    private ZonedDateTime created_date; // It is a better practice, but MariaDB does not support timezones literals.

    public Message() {
    }

    public Message(Integer ID, String message, Integer senderID, Integer receiverID, ZonedDateTime created_date) {
        this.ID = ID;
        this.message = message;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.created_date = created_date;
    }
    //TODO Convert a message to a compatible format to use in UI
    public String toHtmlFormat(Message message) {
        return null;
    }
    public Integer getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(Integer receiverID) {
        this.receiverID = receiverID;
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

    public Integer getSenderID() {
        return senderID;
    }

    public void setSenderID(Integer senderID) {
        this.senderID = senderID;
    }

    public ZonedDateTime getCreated_date() {
        return created_date;
    }

    public void setCreated_date(ZonedDateTime created_date) {
        this.created_date = created_date;
    }
}