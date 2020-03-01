package org.bihe.sevices;

import org.bihe.models.Message;

import java.time.format.DateTimeFormatter;

public class MessageForSending {
    private String message;
    private String senderUsername;
    private String receiverUsername;
    private String time;
    private String date;
    private String type;

    public MessageForSending(Message message) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EE dd MMMM");

        this.message = message.getMessage();
        this.senderUsername = message.getSenderUser().getUsername();
        this.receiverUsername = message.getReceiverUser().getUsername();
        this.time = message.getCreated_date().format(timeFormatter);
        this.date = message.getCreated_date().format(dateFormatter);
        this.type = message.getMessageType();
    }

    public MessageForSending() {
    }
}
