package com.artofcodeapps.locationalarm.app.domain;

import android.content.ContentValues;
import android.database.Cursor;

import com.artofcodeapps.locationalarm.app.services.Database;
import com.artofcodeapps.locationalarm.app.services.DbContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pete on 9.4.2014.
 */
public class ReminderDAO implements Dao, Serializable {
    private List reminders;
    private Database db;

    //todo move the db.getreadabledatabase call to asynctask
    public ReminderDAO(Database db){
        this.db = db;
        reminders = fetchAllFromDatabase();
    }
    public boolean noReminders(){
        return reminders == null || reminders.size()== 0;
    }

    private List fetchAllFromDatabase(){
        return db.getAll(DbContract.ReminderEntry.TABLE_NAME, DbContract.ReminderEntry._ID + " DESC", ReminderDAO.class);
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
        return reminders;
    }

    @Override
    public boolean insert(Object r) {
        Reminder reminder = (Reminder) r;
        if(!reminder.hasContent()){
            return false;
        }
        ContentValues vals = values(reminder);
        long id = db.insert(vals, DbContract.ReminderEntry.TABLE_NAME);
        reminder.setId(id);
        reminders.add(r);
        return id != -1;
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
        Reminder r = new Reminder(content);
        r.setId(id);
        return r;
    }

}
