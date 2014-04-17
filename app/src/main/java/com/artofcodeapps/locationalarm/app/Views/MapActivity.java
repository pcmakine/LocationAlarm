package com.artofcodeapps.locationalarm.app.Views;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.artofcodeapps.locationalarm.app.R;
import com.artofcodeapps.locationalarm.app.services.GeocoderTask;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import java.util.List;
import java.util.Locale;


public class MapActivity extends ActionBarActivity implements GoogleMap.OnMapLongClickListener {
    private LocationManager manager;

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
                    new GeocoderTask(googleMap, getBaseContext()).execute(location);
                }

            }
        });
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
            //    googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setRotateGesturesEnabled(false);
            googleMap.setOnMapLongClickListener(this);
            Location loc = getLocation();
            animateToLocation(loc);

            // check if map is created successfully or not
            if (googleMap == null) {
                new ErrorMessage(R.string.loading_map_error).show(getApplicationContext(), Toast.LENGTH_SHORT);
/*                Toast.makeText(getApplicationContext(),
                        R.string.loading_map_error, Toast.LENGTH_SHORT)
                        .show();*/
            }else{
                Toast.makeText(getApplicationContext(), "Map initialized", Toast.LENGTH_SHORT).show();
            }
        }
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

    @Override
    public void onMapLongClick(LatLng point) {
        Toast.makeText(getApplicationContext(), "long pressed, point=" + point, Toast.LENGTH_LONG).show();
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


