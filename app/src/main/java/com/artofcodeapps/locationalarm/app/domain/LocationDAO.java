package com.artofcodeapps.locationalarm.app.domain;

import android.content.ContentValues;
import android.database.Cursor;

import com.artofcodeapps.locationalarm.app.services.Database;
import com.artofcodeapps.locationalarm.app.services.DbContract;
import com.google.android.gms.maps.model.LatLng;

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
        return db.getAll(DbContract.LocationEntry.TABLE_NAME, DbContract.LocationEntry._ID + " DESC", LocationDAO.class);
    }

    private static LatLng getLatLng(Cursor c){
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
        Location loc = (Location) db.getEntry(DbContract.LocationEntry.TABLE_NAME, DbContract.LocationEntry._ID, id, LocationDAO.class);
        return loc;
    }

    @Override
    public boolean insert(Object data) {
        Location loc = (Location) data;
        ContentValues vals = values(loc);
        long id = db.insert(vals, DbContract.LocationEntry.TABLE_NAME);
        loc.setId(id);
        locations.add(loc);
        return id != -1;
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

    public static Location createOneEntry(Cursor c){
        long id = c.getLong(c.getColumnIndexOrThrow(DbContract.LocationEntry._ID));
        Location loc = new Location(getLatLng(c));
        loc.setId(id);
        return loc;
    }
}

