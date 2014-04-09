package com.artofcodeapps.locationalarm.app.domain;

/**
 * Created by Pete on 9.4.2014.
 */
public class Reminder {

    private long id;
    private String content;

    public Reminder(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }

}
