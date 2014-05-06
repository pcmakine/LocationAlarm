package com.artofcodeapps.locationalarm.app.domain;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Pete on 23.4.2014.
 */
public class ReminderLocation implements Trigger{
    private LatLng location;
    private long id;
    private long reminderID;  //reference to the associated reminder
    private int radiusInMeters;
    private static final int DEFAULT_RADIUS = 500;

    public ReminderLocation(LatLng location){
        this(location, DEFAULT_RADIUS);
    }

    public long getReminderID() {
        return reminderID;
    }

    public void setReminderID(long reminderID) {
        this.reminderID = reminderID;
    }

    public ReminderLocation(LatLng location, int radiusInMeters){

        this.location = location;
        this.radiusInMeters = radiusInMeters;
    }

    public LatLng getLatLng() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRadius(){
        return radiusInMeters;
    }

    @Override
    public boolean checkTriggerCondition() {
        return false;
    }

    @Override
    public String toString(){
        return location.toString();
    }



}
