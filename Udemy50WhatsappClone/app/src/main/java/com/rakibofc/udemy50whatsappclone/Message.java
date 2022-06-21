package com.rakibofc.udemy50whatsappclone;

public class Message {

    String message;
    String messageType;

    public Message(String message, String messageType) {

        this.message = message;
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
