package com.artofcodeapps.locationalarm.app.services;

/**
 * Created by Pete on 24.4.2014.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;

import com.artofcodeapps.locationalarm.app.domain.ReminderLocation;
import com.artofcodeapps.locationalarm.app.domain.LocationDAO;
import com.artofcodeapps.locationalarm.app.domain.Reminder;
import com.artofcodeapps.locationalarm.app.domain.ReminderDAO;

/**
 * The purpose of this class is to provide methods for adding reminders.
 * The methods will also update the relevant alarm and other database fields without the user of
 * this class having to worry about those updates.
 */
public class DataManager {

    //todo test if throwing the exception really works
    public static boolean saveReminder(Reminder reminder, ReminderLocation reminderLocation, Context ctx){
        Database db = new Database(ctx);
        ReminderDAO reminders = new ReminderDAO(db);
        LocationDAO locations = new LocationDAO(db);
        long reminderID, locationID = -1;

        try{
            reminderID = reminders.insert(reminder);
            reminderLocation.setReminderID(reminderID);
            locationID = locations.insert(reminderLocation);
           // insertReminderLocationLink(db, reminderID, locationID);
        }catch(SQLException e){
            return false;
        }
        return reminderID != -1;
    }

    public static ReminderDAO getReminderManager(Context ctx){
        ReminderDAO reminders = new ReminderDAO(new Database(ctx));
        return null;
    }

    public static void insertReminderLocationLink(Database db, long reminderID, long locationID) throws SQLException{
        ContentValues vals = new ContentValues();
        vals.put(DbContract.ReminderLocationLinkEntry.COLUMN_NAME_REMINDERID, reminderID);
        vals.put(DbContract.ReminderLocationLinkEntry.COLUMN_NAME_LOCATIONID, locationID);
        long id = db.insert(vals, DbContract.ReminderLocationLinkEntry.TABLE_NAME);
    }

}
