package com.artofcodeapps.locationalarm.app.domain;

import android.content.ContentValues;
import android.database.Cursor;

import com.artofcodeapps.locationalarm.app.services.Database;
import com.artofcodeapps.locationalarm.app.services.DbContract;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pete on 14.4.2014.
 */
public class LocationDAO implements Dao {

    private List locations;
    private Database db;

    public LocationDAO(Database db){
        this.db = db;
        locations = fetchAllFromDatabase();
    }

    private List fetchAllFromDatabase(){
        Cursor cursor = db.getAll(DbContract.LocationEntry.TABLE_NAME, DbContract.LocationEntry._ID + " DESC");
        return locationsAsList(cursor);
    }
    private ArrayList locationsAsList(Cursor c){
        ArrayList list = new ArrayList();
        if(c.moveToFirst()){
            do{
                Location loc = getOneLocation(c);
                list.add(loc);
            }while(c.moveToNext());
        }
        return list;
    }

    private Location getOneLocation(Cursor c){
        long id = c.getLong(c.getColumnIndexOrThrow(DbContract.LocationEntry._ID));
        Location loc = new Location(getLatLng(c));
        loc.setId(id);
        return loc;
    }

    private LatLng getLatLng(Cursor c){
        long lat = c.getLong(c.getColumnIndexOrThrow(DbContract.LocationEntry.COLUMN_NAME_LAT));
        long longitude = c.getLong(c.getColumnIndexOrThrow(DbContract.LocationEntry.COLUMN_NAME_LONG));
        return new LatLng(lat, longitude);
    }

    @Override
    public List getAll() {
        return locations;
    }

    @Override
    public Object getOne(Long id) {
        Cursor cursor = db.getEntry(DbContract.LocationEntry.TABLE_NAME, DbContract.LocationEntry._ID, id);
        if(cursor == null){
            return null;
        }
        cursor.moveToFirst();

        Location loc = new Location(getLatLng(cursor));
        loc.setId(id);
        return loc;
    }

    @Override
    public boolean insert(Object data) {
        Location loc = (Location) data;
        ContentValues vals = values(loc);
        long id = db.insert(vals, DbContract.LocationEntry.TABLE_NAME);
        updateLocationAndList(loc, id);
        return id != -1;
    }

    private void updateLocationAndList(Location loc, long id){
        loc.setId(id);
        locations.add(loc);
    }

    public ContentValues values(Location loc){
        ContentValues vals = new ContentValues();
        vals.put(DbContract.LocationEntry.COLUMN_NAME_LAT, loc.getLocation().latitude);
        vals.put(DbContract.LocationEntry.COLUMN_NAME_LONG, loc.getLocation().longitude);
        return vals;
    }

    @Override
    public boolean update(Object newData) {
        Location loc = (Location) newData;
        ContentValues vals = values(loc);
        int numOfRowsAffected = db.update(vals, DbContract.LocationEntry.TABLE_NAME, DbContract.LocationEntry._ID, String.valueOf(loc.getId()));
        return numOfRowsAffected > 0;
    }

    @Override
    public boolean remove(Object data) {
        Location loc = (Location) data;
        return db.deleteEntry(loc.getId(), DbContract.LocationEntry.TABLE_NAME, DbContract.LocationEntry._ID);
    }

    @Override
    public boolean removeAll() {
        return false;
    }
}
