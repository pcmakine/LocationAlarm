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
    private List<Location> locations;

    public Reminder(String content) {
        this.content = content;
        this.locations = new ArrayList();
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

    public void setLocations(List locations){
        if(locations != null){
            this.locations = locations;
        }
    }

    public boolean hasContent(){
        return content != null && !content.trim().equals(new String(""));
    }

    @Override
    public String toString() {
        return content;
    }

    public List<Location> getLocations() {
        return locations;
    }
}
