package com.artofcodeapps.locationalarm.app.services;

import android.content.Context;
import android.graphics.Color;

import com.artofcodeapps.locationalarm.app.domain.Reminder;
import com.artofcodeapps.locationalarm.app.domain.ReminderDAO;
import com.artofcodeapps.locationalarm.app.domain.ReminderLocation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Pete on 12.5.2014.
 */
public class MarkerManager {
    private static final float SELECTED_COLOR = BitmapDescriptorFactory.HUE_AZURE;
    private static final float NON_EMPTY_MARKER_ON = BitmapDescriptorFactory.HUE_ORANGE;
    private static final float NON_EMPTY_MARKER_OFF = BitmapDescriptorFactory.HUE_VIOLET;
    private static final float EMPTY_MARKER_COLOR = BitmapDescriptorFactory.HUE_RED;
    private GoogleMap map;
    private HashMap<Marker, Reminder> markerReminderMap;
    private ReminderDAO reminders;
    private Circle activeRadius;
    private Marker selectedMarker;

    public MarkerManager(GoogleMap map, Context context){
        this.map = map;
        this.markerReminderMap = new HashMap();
        populateMapWithMarkers(context);
        this.selectedMarker = null;
    }

    public void populateMapWithMarkers(Context context){
        reminders = new ReminderDAO(new Database(context));
        List<Reminder> reminderList = reminders.getAll();
        for (Reminder reminder: reminderList){
            saveMarker(generateMarker(reminder.getContent(), reminder.getLocation().getLatLng(), NON_EMPTY_MARKER_ON), reminder);
        }
        updateMarkerColors();
    }

    public Marker generateMarker(String text, LatLng loc, float color){
        return (map.addMarker(new MarkerOptions()
                .position(loc)
                .title(StringUtils.abbreviate(text, 20))
                .icon(BitmapDescriptorFactory.defaultMarker(color))
                .draggable(true)));
    }

    public void saveMarker(Marker marker, Reminder reminder){
        //  showRadius(loc);
        markerReminderMap.put(marker, reminder);
    }

    private void showRadius(ReminderLocation loc){
        CircleOptions circleOptions = new CircleOptions()
                .center(loc.getLatLng())   //set center
                .radius(loc.getRadius())   //set radius in meters
                .fillColor(Color.argb(50, 20, 134, 255))  //default
                .strokeColor(Color.BLUE)
                .strokeWidth(5);
        activeRadius = map.addCircle(circleOptions);
    }

    public void updateMarkerColors(){
        Set<Marker> markersOnMap = markerReminderMap.keySet();
        for(Marker marker: markersOnMap){
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(getMarkerColor(marker)));
        }
       // removeRadiusFromMap();
    }

    private float getMarkerColor(Marker marker){
        if(marker.equals(selectedMarker)){
            return SELECTED_COLOR;
        }
        else if(markerReminderMap.get(marker) == null){
            return EMPTY_MARKER_COLOR;
        }else if(markerReminderMap.get(marker).isOn()){
            return NON_EMPTY_MARKER_ON;
        }else {
            return NON_EMPTY_MARKER_OFF;
        }
    }

    public void removeRadiusFromMap(){
        if(activeRadius != null){
            activeRadius.remove();
        }
    }

    public void selectMarker(Marker marker){
        selectedMarker = marker;
        int markerHash = marker.hashCode();
        Marker mapMarker = markerReminderMap.keySet().iterator().next();
        int mapMarkerHash = mapMarker.hashCode();
        int selectedHash = selectedMarker.hashCode();
        updateMarkerColors();
        removeRadiusFromMap();
        if(markerReminderMap.get(marker) != null && markerReminderMap.get(marker).getLocation() != null){
            showMarkerInfoWindow(marker);
            showRadius(markerReminderMap.get(marker).getLocation());
        }
    }

    public void unSelectMarker(){
        selectedMarker = null;
        updateMarkerColors();
        removeRadiusFromMap();
    }

    private void showMarkerInfoWindow(Marker marker){
        Reminder reminder = markerReminderMap.get(marker);
        if(reminder != null && reminder.getLocation() != null){
            marker.setTitle(StringUtils.abbreviate(reminder.getContent(), 20));
            marker.showInfoWindow();
        }
    }

    public boolean removeSelectedIfEmpty(){
        if(markerReminderMap.get(selectedMarker) == null){
            selectedMarker.remove();
            markerReminderMap.remove(selectedMarker);
            return true;
        }
        return false;
    }

    public void removeSelectedMarker(){
        markerReminderMap.remove(selectedMarker); //todo what happens if the program stops here, before the next command
        selectedMarker.remove();
        selectedMarker = null;
        removeRadiusFromMap();
    }

    public Reminder getReminder(Marker marker){
        if(marker != null){
            return markerReminderMap.get(marker);
        }
        return null;
    }

    public Marker getSelectedMarker(){
        return selectedMarker;
    }

    public boolean userHasSelectedMarker(){
        return selectedMarker != null;
    }

    public boolean userHasSelectedEmptyMarker(){
        return userHasSelectedMarker() && markerReminderMap.get(selectedMarker) == null;
    }

    public static float getSelectedColor(){
        return SELECTED_COLOR;
    }

}
