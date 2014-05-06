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

    @Override
    public List getAll() {
        return locations;
    }

    @Override
    public Object getOne(Long id) {
        ReminderLocation loc = (ReminderLocation) db.getEntry(DbContract.LocationEntry.TABLE_NAME, DbContract.LocationEntry._ID, id, LocationDAO.class);
        return loc;
    }

    @Override
    public long insert(Object data) {
        ReminderLocation loc = (ReminderLocation) data;
        ContentValues vals = values(loc);
        long id = db.insert(vals, DbContract.LocationEntry.TABLE_NAME);
        loc.setId(id);
        locations.add(loc);
        return id;
    }

    public ContentValues values(ReminderLocation loc){
        ContentValues vals = new ContentValues();
        vals.put(DbContract.LocationEntry.COLUMN_NAME_LAT, loc.getLatLng().latitude);
        vals.put(DbContract.LocationEntry.COLUMN_NAME_LONG, loc.getLatLng().longitude);
        vals.put(DbContract.LocationEntry.COLUMN_NAME_REMINDER_ID, loc.getReminderID());
        return vals;
    }

    @Override
    public boolean update(Object newData) {
        ReminderLocation loc = (ReminderLocation) newData;
        ContentValues vals = values(loc);
        int numOfRowsAffected = db.update(vals, DbContract.LocationEntry.TABLE_NAME, DbContract.LocationEntry._ID, String.valueOf(loc.getId()));
        return numOfRowsAffected > 0;
    }

    @Override
    public boolean remove(Object data) {
        ReminderLocation loc = (ReminderLocation) data;
        return db.deleteEntry(loc.getId(), DbContract.LocationEntry.TABLE_NAME, DbContract.LocationEntry._ID);
    }

    @Override
    public boolean removeAll() {
        return false;
    }

    public static ReminderLocation createOneEntry(Cursor c){
        long id = c.getLong(c.getColumnIndexOrThrow(DbContract.LocationEntry._ID));
        int index = c.getColumnIndexOrThrow(DbContract.LocationEntry.COLUMN_NAME_REMINDER_ID);
        long reminderID = c.getLong(index);
        ReminderLocation loc = new ReminderLocation(getLatLng(c));
        loc.setId(id);
        loc.setReminderID(reminderID);
        return loc;
    }

    private static LatLng getLatLng(Cursor c){
        double lat = c.getDouble(c.getColumnIndexOrThrow(DbContract.LocationEntry.COLUMN_NAME_LAT));
        double longitude = c.getDouble(c.getColumnIndexOrThrow(DbContract.LocationEntry.COLUMN_NAME_LONG));
        return new LatLng(lat, longitude);
    }
}

