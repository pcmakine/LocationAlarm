package com.artofcodeapps.locationalarm.app.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.artofcodeapps.locationalarm.app.services.Database;
import com.artofcodeapps.locationalarm.app.services.DbContract;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Pete on 9.4.2014.
 */
public class ReminderDAO implements Dao, Serializable {
  //  private List<Reminder> reminders;
    private static Database db;

    //todo move the db.getreadabledatabase call to asynctask
    public ReminderDAO(Database database){
        db = database;
       // reminders = fetchAllFromDatabase();
    }
    public boolean noReminders(){
        return fetchAllFromDatabase().isEmpty();
    }


    private List fetchAllFromDatabase(){
        List<Reminder> list = db.getAll(DbContract.ReminderEntry.TABLE_NAME, DbContract.ReminderEntry._ID + " DESC", ReminderDAO.class);
        return list;
    }

    private static ReminderLocation getReminderLocation(long reminderID){
        String sql = "SELECT * FROM locations WHERE locations.reminderID = " +
                reminderID;
      //  String sql = "SELECT locations._id, locations.lat, locations.long FROM locations, links, reminders where locations._id = links.location_id AND " + reminderID + " = links.reminder_id";
        ReminderLocation location = (ReminderLocation) db.executeRaw(sql, LocationDAO.class);
        return location;
    }

    @Override
    public boolean remove(Object d) {
        Reminder reminder = (Reminder) d;
        return db.deleteEntry(reminder.getId(), DbContract.ReminderEntry.TABLE_NAME, DbContract.ReminderEntry._ID);
    }

    @Override
    public boolean removeAll() {
        return false;
    }

    @Override
    public Object getOne(Long id) {
        Reminder reminder = (Reminder) db.getEntry(DbContract.ReminderEntry.TABLE_NAME, DbContract.ReminderEntry._ID, id, ReminderDAO.class);
        return reminder;
    }

    @Override
    public List getAll() {
        return fetchAllFromDatabase();
    }

    @Override
    public long insert(Object r) throws SQLException {
        Reminder reminder = (Reminder) r;
        if(!reminder.hasContent()){
            return -1;
        }
        ContentValues vals = values(reminder);
        long id = db.insert(vals, DbContract.ReminderEntry.TABLE_NAME);
        reminder.setId(id);
        //reminders.add(reminder);
        return id;
    }

    public ContentValues values(Reminder reminder){
        ContentValues vals = new ContentValues();
        vals.put(DbContract.ReminderEntry.COLUMN_NAME_CONTENT, reminder.getContent());
        return vals;
    }

    @Override
    public boolean update(Object r) {
        Reminder reminder = (Reminder) r;
        if(!reminder.hasContent()){
            return false;
        }
        ContentValues vals = values(reminder);
        int numOfRowsAffected = db.update(vals, DbContract.ReminderEntry.TABLE_NAME, DbContract.ReminderEntry._ID, String.valueOf(reminder.getId()));
        return numOfRowsAffected > 0;
    }


    public static Reminder createOneEntry(Cursor c){
        long id = c.getLong(c.getColumnIndexOrThrow(DbContract.ReminderEntry._ID));
        String content = c.getString(c.getColumnIndexOrThrow(DbContract.ReminderEntry.COLUMN_NAME_CONTENT));
        Reminder r = new Reminder(id, content);
        r.setReminderLocation(getReminderLocation(id));
        return r;
    }

    //todo make this more efficient
/*    public Reminder getReminderFromMemory(long id){
        for(Reminder r: reminders){
            if(r.getId() == id){
                return r;
            }
        }
        return null;
    }*/

}
