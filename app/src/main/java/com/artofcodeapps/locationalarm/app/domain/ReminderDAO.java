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
        Cursor cursor = db.getAll(DbContract.ReminderEntry.TABLE_NAME, DbContract.ReminderEntry._ID + " DESC");
        return remindersAsList(cursor);
    }
    private ArrayList remindersAsList(Cursor c){
        ArrayList list = new ArrayList();
        if(c.moveToFirst()){
            do{
                Reminder r = getOneReminder(c);
                list.add(r);
            }while(c.moveToNext());
        }
        return list;
    }

    private Reminder getOneReminder(Cursor c){
        long id = c.getLong(c.getColumnIndexOrThrow(DbContract.ReminderEntry._ID));
        String content = c.getString(c.getColumnIndexOrThrow(DbContract.ReminderEntry.COLUMN_NAME_CONTENT));
        Reminder r = new Reminder(content);
        r.setId(id);
        return r;
    }

    @Override
    public Object getOne(Long id) {
        Cursor cursor = db.getEntry(DbContract.ReminderEntry.TABLE_NAME, DbContract.ReminderEntry._ID, id);
        if(cursor == null){
            return null;
        }
        cursor.moveToFirst();
        Reminder reminder = new Reminder(cursor.getString(
                cursor.getColumnIndexOrThrow(DbContract.ReminderEntry.COLUMN_NAME_CONTENT)));
        reminder.setId(id);
        return reminder;
    }

    private void updateReminderAndList(Reminder r, long id){
        r.setId(id);
        reminders.add(r);
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
        updateReminderAndList(reminder, id);
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

    @Override
    public boolean remove(Object d) {
        Reminder reminder = (Reminder) d;
        return db.deleteEntry(reminder.getId(), DbContract.ReminderEntry.TABLE_NAME, DbContract.ReminderEntry._ID);
    }

    @Override
    public boolean removeAll() {
        return false;
    }

}
