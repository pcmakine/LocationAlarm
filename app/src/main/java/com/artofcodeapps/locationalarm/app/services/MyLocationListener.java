package com.artofcodeapps.locationalarm.app.services;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.artofcodeapps.locationalarm.app.Views.MapActivity;
import com.artofcodeapps.locationalarm.app.domain.Reminder;
import com.artofcodeapps.locationalarm.app.domain.ReminderDAO;

import java.util.List;

/**
 * Created by Pete on 11.5.2014.
 */
public class MyLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        ReminderDAO reminders = new ReminderDAO(new Database(MapActivity.appCtx));
        List<Reminder> list = reminders.getAll();
        for(Reminder reminder: list){
            float[] results = new float[3];
            Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                    reminder.getLocation().getLatLng().latitude,
                    reminder.getLocation().getLatLng().longitude, results);

            float distance = results[0];
            if(distance < reminder.getLocation().getRadius()){
            }
            Toast.makeText(MapActivity.appCtx, "Distance from current location: " + distance +
            " meters", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
