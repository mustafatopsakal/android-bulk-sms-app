package com.example.mustafa.toplusms;

/**
 * Created by mustafa on 18.09.2016.
 */
public class Message {
    String number,date,message;

    public Message(String number, String date, String message) {
        this.number = number;
        this.date = date;
        this.message = message;
    }

    public String getNumber() {
        return number;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }
}
