package com.driver;

import java.util.Date;

public class Message {
    private int id;
    private String content;
    private Date timestamp;

    Message(){};
    Message(int id, String content, Date timestamp){
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
