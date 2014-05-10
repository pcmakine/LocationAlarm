package com.artofcodeapps.locationalarm.app.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import com.artofcodeapps.locationalarm.app.R;
import com.artofcodeapps.locationalarm.app.domain.*;
import com.artofcodeapps.locationalarm.app.services.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.*;


public class MapActivity extends ActionBarActivity implements GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener, View.OnDragListener{
    private static final float SELECTED_COLOR = BitmapDescriptorFactory.HUE_AZURE;
    private static final float MARKER_WITH_REMINDER_COLOR = BitmapDescriptorFactory.HUE_ORANGE;
    private static final float EMPTY_MARKER_COLOR = BitmapDescriptorFactory.HUE_RED;
    private LocationManager manager;
    private HashMap<Marker, Reminder> markerReminderMap;
    private Marker selectedMarker;
    private ReminderDAO reminders;
    private Circle activeRadius;
    private boolean markerDragRestarted;

    // Google Map
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Button findLocationBtn = (Button) findViewById(R.id.findLocationBtn);
        findLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText locationInput = (EditText) findViewById(R.id.locationInput);
                String location = locationInput.getText().toString();
                if(location!=null && !location.equals("")){
                    new GeocoderTask(googleMap, getBaseContext(), MapActivity.this).execute(location);
                }

            }
        });
    }
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initializeMap() {
        if (googleMap != null) {
            googleMap.clear();
        }
        else{
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            googleMap.setMyLocationEnabled(true);
            //    googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setRotateGesturesEnabled(false);
            googleMap.setOnMapLongClickListener(this);
            googleMap.setOnMapClickListener(this);
            googleMap.setOnMarkerClickListener(this);
            googleMap.setOnInfoWindowClickListener(this);
            googleMap.setOnMarkerDragListener(this);
            Location loc = getLocation();
            animateToLocation(loc);

            // check if map is created successfully or not
            if (googleMap == null) {
                new ErrorMessage(R.string.loading_map_error).show(getApplicationContext(), Toast.LENGTH_SHORT);
/*                Toast.makeText(getApplicationContext(),
                        R.string.loading_map_error, Toast.LENGTH_SHORT)
                        .show();*/
            }
        }
    }
    public void populateMapWithMarkers(){
        reminders = new ReminderDAO(new Database(this));
        List<Reminder> reminderList = reminders.getAll();
        for (Reminder reminder: reminderList){
            placeMarker(reminder, reminder.getContent(), reminder.getLocation().getLatLng(), MARKER_WITH_REMINDER_COLOR);
        }
    }


    private Marker placeMarker(Reminder r, String text, LatLng loc, float color){
        Marker m = (googleMap.addMarker(new MarkerOptions()
                .position(loc)
                .title(StringUtils.abbreviate(text, 20))
                .icon(BitmapDescriptorFactory.defaultMarker(color))
                .draggable(true)));
        //  showRadius(loc);
        markerReminderMap.put(m, r);
        return m;
    }

    private void showRadius(ReminderLocation loc){
        CircleOptions circleOptions = new CircleOptions()
                .center(loc.getLatLng())   //set center
                .radius(loc.getRadius())   //set radius in meters
                .fillColor(Color.argb(50, 20, 134, 255))  //default
                .strokeColor(Color.BLUE)
                .strokeWidth(5);
        activeRadius = googleMap.addCircle(circleOptions);
    }

    private void animateToLocation(Location loc){
        CameraPosition pos = new CameraPosition.Builder().target(new LatLng(loc.getLatitude(), loc.getLongitude())).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos));
    }

    private Location getLocation(){
        int minTime = 0;
        List<String> matchingProviders = manager.getAllProviders();
        String prov = matchingProviders.get(0);
        Location loc = manager.getLastKnownLocation(prov);
        float bestAccuracy = loc.getAccuracy();
        Location bestResult = loc;
        long bestTime = loc.getTime();

        for (String provider: matchingProviders) {
            Location location = manager.getLastKnownLocation(provider);

            if (location != null) {
                float accuracy = location.getAccuracy();
                long time = location.getTime();

                if ((time > minTime && accuracy < bestAccuracy)) {
                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestTime = time;
                }
                else if (time < minTime &&
                        bestAccuracy == Float.MAX_VALUE && time > bestTime){
                    bestResult = location;
                    bestTime = time;
                }
            }
        }
        return bestResult;
    }

    public void setMarker(Marker marker){
        this.selectedMarker = marker;
        markerReminderMap.put(selectedMarker, null);
    }

    //todo show the search field. If the search button is not pressed do not show it at all
    public void showSearch(){

    }

    public void startAddActivity(){
        if(selectedMarker == null){
            Toast.makeText(this, "No location chosen!", Toast.LENGTH_SHORT);
        }else{
            Intent intent = new Intent(this, EditAddActivity.class);
            intent.putExtra("location", selectedMarker.getPosition());
            this.startActivity(intent);
        }
    }

    public void showNoMarkerAsSelected(){
        Set<Marker> markersOnMap = markerReminderMap.keySet();
        for(Marker marker: markersOnMap){
            if(markerReminderMap.get(marker) == null){
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(EMPTY_MARKER_COLOR));
            }else{
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(MARKER_WITH_REMINDER_COLOR));
            }
        }
        removeRadiusFromMap();
    }

    private void removeRadiusFromMap(){
        if(activeRadius != null){
            activeRadius.remove();
        }
    }

    public void startEdit(Reminder r){
        Intent intent = new Intent(this, EditAddActivity.class);
        intent.putExtra("reminderID", r.getId());
        startActivity(intent);
    }

    private void showClickedMarkerAsSelected(Marker marker){
        selectedMarker = marker;
        showNoMarkerAsSelected();
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(SELECTED_COLOR));
        if(markerReminderMap.get(marker) != null && markerReminderMap.get(marker).getLocation() != null){
            showMarkerInfoWindow(marker);
            showRadius(markerReminderMap.get(marker).getLocation());
        }
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        showClickedMarkerAsSelected(marker);
        // marker.remove();
        return true;
    }

    private void showMarkerInfoWindow(Marker marker){
        Reminder reminder = markerReminderMap.get(marker);
        if(reminder != null && reminder.getLocation() != null){
            marker.setTitle(StringUtils.abbreviate(reminder.getContent(), 20));
            marker.showInfoWindow();
        }
    }

    @Override
    public void onMapLongClick(LatLng point) {
        showNoMarkerAsSelected();
        this.selectedMarker = placeMarker(null, "No reminder", point, SELECTED_COLOR);
        supportInvalidateOptionsMenu();
    }

    private void confirmRemove(){
        if(selectedMarker != null){
            if(markerReminderMap.get(selectedMarker) == null){
                removeSelectedMarker();
                supportInvalidateOptionsMenu();
            }else{
                showConfirmationPopUp();
            }
        }
    }

    private void showConfirmationPopUp(){
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this reminder?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        reminders.remove(markerReminderMap.get(selectedMarker));
                        removeSelectedMarker();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void removeSelectedMarker(){
        markerReminderMap.remove(selectedMarker); //todo what happens if the program stops here, before the next command
        selectedMarker.remove();
        selectedMarker = null;
        removeRadiusFromMap();
        supportInvalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        this.markerReminderMap = new HashMap();
        initializeMap();
        populateMapWithMarkers();
        this.selectedMarker = null;
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_activity_actions, menu);
        if(selectedMarker == null || markerReminderMap.get(selectedMarker) != null){
            MenuItem item = menu.findItem(R.id.action_add_alarm);
            item.setVisible(false);
        }
        //       mOptionsMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem item = menu.findItem(R.id.action_add_alarm);
        if(selectedMarker == null || markerReminderMap.get(selectedMarker) != null){
            item.setVisible(false);
        }else{
            item.setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_search:
                showSearch();
                return true;
            case R.id.action_add_alarm:
                startAddActivity();
                return true;
            case R.id.action_discard:
                confirmRemove();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        showNoMarkerAsSelected();
        this.selectedMarker = null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Reminder reminder = markerReminderMap.get(marker);
        if(reminder != null){
            startEdit(reminder);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        removeRadiusFromMap();
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Reminder reminder = markerReminderMap.get(marker);
        if(reminder != null){
            ReminderLocation loc = reminder.getLocation();
            loc.setLocation(marker.getPosition());
            LocationDAO locations = new LocationDAO(new Database (this));
            locations.update(loc);
            showRadius(loc);
            showClickedMarkerAsSelected(marker);
        }
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        return false;
    }
}


