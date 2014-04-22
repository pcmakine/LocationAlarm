package com.artofcodeapps.locationalarm.app.domain;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Pete on 23.4.2014.
 */
public class Location {
    private LatLng location;
    private long id;

    public Location(LatLng location){
        this.location = location;
    }

    public LatLng getLocation() {
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

}
