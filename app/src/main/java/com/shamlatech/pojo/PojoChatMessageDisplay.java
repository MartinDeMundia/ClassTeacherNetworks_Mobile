package com.shamlatech.pojo;

/**
 * Created by Shamla Tech on 16-05-2018.
 */

public class PojoChatMessageDisplay {

    String message_id;
    String sender_id;
    String receiver_id;
    String message;
    String sent_on;
    String receive_on;
    String read_on;
    String type;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSent_on() {
        return sent_on;
    }

    public void setSent_on(String sent_on) {
        this.sent_on = sent_on;
    }

    public String getReceive_on() {
        return receive_on;
    }

    public void setReceive_on(String receive_on) {
        this.receive_on = receive_on;
    }

    public String getRead_on() {
        return read_on;
    }

    public void setRead_on(String read_on) {
        this.read_on = read_on;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
