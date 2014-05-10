package com.artofcodeapps.locationalarm.app.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pete on 9.4.2014.
 */
public class Reminder implements Data{
    private long id;
    private String content;
    private Alarm alarm;
    private ReminderLocation reminderLocation;
    private boolean on;

    public Reminder(String content) {
        this.content = content;
    }

    public Reminder(long id, String content, boolean on){
        this.id = id;
        this.content = content;
        this.on = on;
    }

    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAlarm(Alarm alarm){
        this.alarm = alarm;
    }

    public Alarm getAlarm(){
        return this.alarm;
    }

    public void setReminderLocation(ReminderLocation loc){
        this.reminderLocation = loc;
    }

    public boolean hasContent(){
        return content != null && !content.trim().equals(new String(""));
    }

    public boolean isOn(){
        return on;
    }

    public void turnOn(){
        this.on = true;
    }

    public void turnOff(){
        this.on = false;
    }

    @Override
    public String toString() {
        return content;
    }

    public ReminderLocation getLocation() {
        return reminderLocation;
    }
}
