package com.artofcodeapps.locationalarm.app.Views;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.artofcodeapps.locationalarm.app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class MapActivity extends ActionBarActivity {
    private LocationManager manager;

    // Google Map
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        initializeMap();
    }
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setRotateGesturesEnabled(false);
            Location loc = getLocation();

            CameraPosition pos = new CameraPosition.Builder().target(new LatLng(loc.getLatitude(), loc.getLongitude())).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos));
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        initializeMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}